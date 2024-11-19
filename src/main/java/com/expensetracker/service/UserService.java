package com.expensetracker.service;

import com.expensetracker.dto.UserDTO;
import com.expensetracker.entity.User;
import com.expensetracker.exceptions.UserNotFoundException;
import com.expensetracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
        return userRepository.findByCognitoId(cognitoUuid)
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
}
