package com.expensetracker.service;

import com.expensetracker.dto.UserDTO;
import com.expensetracker.entity.User;
import com.expensetracker.exceptions.UserNotFoundException;
import com.expensetracker.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CognitoService cognitoService; // Inject the CognitoService

    /**
     * Registers a new user in Cognito and synchronizes with the local database.
     *
     * @param userDTO the user data transfer object containing new user data.
     * @return the created user DTO with details filled in from the local DB.
     */
    public UserDTO addUser(UserDTO userDTO) {
        String password = userDTO.getPassword(); // Make sure to securely collect and handle the password.
        String cognitoUuid = cognitoService.registerUserWithCognito(userDTO.getUsername(), password, userDTO.getEmail());

        if (cognitoUuid == null) {
            throw new RuntimeException("Failed to register user with Cognito or user confirmation pending.");
        }

        userDTO.setCognitoUuid(cognitoUuid);
        User user = new User();
        mapUserDTOToUser(userDTO, user);
        user = userRepository.save(user);
        return convertToDTO(user);
    }

    /**
     * Authenticates a user via Cognito and retrieves user details from the local database.
     *
     * @param email    the user's email.
     * @param password the user's password.
     * @return the authenticated user's DTO if successful.
     */
    public UserDTO login(String email, String password) {
        String cognitoUuid = cognitoService.authenticate(email, password);
        if (cognitoUuid == null) {
            throw new RuntimeException("Login failed with Cognito.");
        }
        return userRepository.findByCognitoUuid(cognitoUuid)
                .map(this::convertToDTO)
                .orElseThrow(() -> new UserNotFoundException("User not found in local database."));
    }

    /**
     * Retrieves all users.
     *
     * @return a list of all users as DTOs.
     */
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    /**
     * Converts a User entity to a UserDTO.
     *
     * @param user the user entity.
     * @return the user DTO.
     */
    private UserDTO convertToDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getCognitoUuid(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.isActive()
        );
    }

    /**
     * Maps properties from UserDTO to User entity.
     *
     * @param dto the UserDTO.
     * @param user the User entity.
     */
    private void mapUserDTOToUser(UserDTO dto, User user) {
        user.setCognitoUuid(dto.getCognitoUuid());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setActive(dto.isActive());
    }

    /**
     * Maps properties from a UserDTO to a User entity. This method is used to transfer data
     * from the DTO used in API communication to the entity used for database operations.
     *
     * @param dto The UserDTO containing the data.
     * @param user The User entity to be populated with data from the DTO.
     */
    private void mapDTOToUser(UserDTO dto, User user) {
        user.setCognitoUuid(dto.getCognitoUuid()); // Ensure Cognito UUID is always set from DTO to user
        user.setUsername(dto.getUsername()); // Set username from DTO
        user.setEmail(dto.getEmail()); // Set email from DTO
        user.setFirstName(dto.getFirstName()); // Set first name from DTO
        user.setLastName(dto.getLastName()); // Set last name from DTO
        user.setActive(dto.isActive()); // Set active status from DTO
    }


    /**
     * Retrieves a user by their Cognito UUID and converts them to a Data Transfer Object (DTO).
     * This method searches the database for a user with the specified Cognito UUID.
     *
     * @param cognitoId The Cognito UUID associated with the user.
     * @return UserDTO The user data transfer object if found.
     * @throws RuntimeException If no user is found with the provided Cognito UUID.
     */
    public UserDTO findUserByCognitoId(String cognitoId) {
        Optional<User> user = userRepository.findByCognitoUuid(cognitoId);
        if (user.isPresent()) {
            return convertToDTO(user.get());
        } else {
            throw new RuntimeException("User not found with Cognito ID: " + cognitoId);
        }
    }


    /**
     * Deletes a user identified by their Cognito UUID. This method first checks if the user
     * exists in the database. If the user is found, they are deleted. If not, a runtime exception
     * is thrown.
     *
     * @param cognitoUuid The Cognito UUID of the user to delete.
     * @throws RuntimeException If no user is found with the provided Cognito UUID, indicating that
     * the deletion cannot proceed.
     */
    @Transactional
    public void deleteUserByCognitoId(String cognitoUuid) {
        // Attempt to find the user by Cognito UUID
        Optional<User> userOptional = userRepository.findByCognitoUuid(cognitoUuid);

        if (userOptional.isPresent()) {
            // If user exists, delete them
            userRepository.delete(userOptional.get());
        } else {
            // Throw exception if user does not exist
            throw new RuntimeException("User not found with Cognito UUID: " + cognitoUuid);
        }
    }


    /**
     * Saves or updates a user based on the provided UserDTO. If a user with the given Cognito UUID exists,
     * it updates the existing user; otherwise, it creates a new user.
     *
     * @param userDTO The data transfer object containing user information.
     * @return UserDTO The persisted user data transferred back into DTO form.
     */
    public UserDTO saveOrUpdateUser(UserDTO userDTO) {
        // Fetch user by Cognito UUID to check if it already exists
        Optional<User> existingUser = userRepository.findByCognitoUuid(userDTO.getCognitoUuid());
        User user = existingUser.orElse(new User()); // Create new user if not found
        mapDTOToUser(userDTO, user); // Map DTO to user entity
        user = userRepository.save(user); // Save the user entity
        return convertToDTO(user); // Convert entity back to DTO
    }


}
