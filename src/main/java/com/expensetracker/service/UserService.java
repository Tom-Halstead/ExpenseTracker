package com.expensetracker.service;

import com.expensetracker.dto.RegistrationResult;
import com.expensetracker.dto.UserDTO;
import com.expensetracker.entity.User;
import com.expensetracker.exceptions.*;
import com.expensetracker.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CognitoService cognitoService; // Inject the CognitoService

    /**
     * Registers a new user in Cognito and synchronizes with the local database.
     *
     * @param userDTO the user data transfer object containing new user data.
     * @return the created user DTO with details filled in from the local DB.
     * @throws UserRegistrationException if there is an issue with user registration.
     */
    @Transactional
    public RegistrationResult addUser(UserDTO userDTO) {
        validateUserData(userDTO);
        log.debug("Starting registration process for username: {}", userDTO.getUsername());

        String password = userDTO.getPassword();
        String cognitoUuid = cognitoService.registerUserWithCognito(userDTO.getUsername(), password, userDTO.getEmail());

        if (cognitoUuid == null) {
            log.info("User registration pending confirmation for username: {}", userDTO.getUsername());
            return new RegistrationResult("PENDING_CONFIRMATION", "Please confirm your email to complete registration for: " + userDTO.getUsername(), userDTO.getUsername());
        }

        try {
            userDTO.setCognitoUuid(cognitoUuid);
            User user = mapDTOToUser(userDTO, new User());
            userRepository.save(user);
            log.info("User registered successfully with UUID: {}", cognitoUuid);
            return new RegistrationResult("SUCCESS", "User registered successfully for: " + userDTO.getUsername(), userDTO.getUsername());
        } catch (DataIntegrityViolationException ex) {
            log.error("Database integrity violation for user {}: {}", userDTO.getUsername(), ex.getMessage());
            return new RegistrationResult("ERROR", "Database integrity issue during registration for: " + userDTO.getUsername(), userDTO.getUsername());
        } catch (Exception ex) {
            log.error("Unexpected error during registration for user {}: {}", userDTO.getUsername(), ex.getMessage());
            return new RegistrationResult("ERROR", "Unexpected error during user registration for: " + userDTO.getUsername(), userDTO.getUsername());
        }
    }


    public UserDTO fetchUserDTOFromResult(RegistrationResult result) {
        if (result == null || result.getUsername() == null) {
            throw new IllegalArgumentException("Registration result or username cannot be null.");
        }
        return findByUsername(result.getUsername());
    }

    private void validateUserData(UserDTO userDTO) {
        if (userDTO == null) {
            throw new IllegalArgumentException("User data cannot be null");
        }

        // Validate username
        if (userDTO.getUsername() == null || userDTO.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username is required and cannot be blank");
        }

        // Validate email format
        if (userDTO.getEmail() == null || !isValidEmail(userDTO.getEmail())) {
            throw new IllegalArgumentException("Invalid email address");
        }

        // Validate password
        if (userDTO.getPassword() == null || !isValidPassword(userDTO.getPassword())) {
            throw new IllegalArgumentException("Password must meet complexity requirements");
        }

        // Example: Check if username or email already exists
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new IllegalArgumentException("Username is already taken");
        }

        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new IllegalArgumentException("Email is already in use");
        }

    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[\\w-\\.]+@[\\w-]+\\.[a-z]{2,4}$";
        return email != null && email.matches(emailRegex);
    }

    private boolean isValidPassword(String password) {
        // Example: Password must be at least 8 characters and contain one digit and one special character
        String passwordRegex = "^(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,}$";
        return password != null && password.matches(passwordRegex);
    }

    /**
     * Retrieves a user by username.
     *
     * @param username the username to search for
     * @return UserDTO containing user details or null if user is not found
     */
    @Transactional
    public UserDTO findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return convertToDTO(user);
        } else {
            log.info("No user found with username: {}", username);
            return null;
        }
    }


    /**
     * Authenticates a user via Cognito and retrieves user details from the local database.
     *
     * @param email    the user's email.
     * @param password the user's password.
     * @return the authenticated user's DTO if successful.
     */
    public UserDTO login(String email, String password) {
        try {
            String cognitoUuid = cognitoService.authenticate(email, password);
            if (cognitoUuid == null) {
                throw new AuthenticationException("Unable to retrieve Cognito UUID.");
            }
            return userRepository.findByCognitoUuid(cognitoUuid)
                    .map(this::convertToDTO)
                    .orElseThrow(() -> new UserNotFoundException("User with UUID " + cognitoUuid + " not found in local database."));
        } catch (CognitoServiceException e) {
            throw new AuthenticationException("Cognito authentication failed: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new ServiceException("An unexpected error occurred during login", e);
        }
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
     * Maps properties from a UserDTO to a User entity. This method is used to transfer data
     * from the DTO used in API communication to the entity used for database operations.
     *
     * @param dto The UserDTO containing the data.
     * @param user The User entity to be populated with data from the DTO.
     */
    private User mapDTOToUser(UserDTO dto, User user) {
        user.setCognitoUuid(dto.getCognitoUuid()); // Ensure Cognito UUID is always set from DTO to user
        user.setUsername(dto.getUsername()); // Set username from DTO
        user.setEmail(dto.getEmail()); // Set email from DTO
        user.setFirstName(dto.getFirstName()); // Set first name from DTO
        user.setLastName(dto.getLastName()); // Set last name from DTO
        user.setActive(dto.isActive()); // Set active status from DTO
        return user;
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
