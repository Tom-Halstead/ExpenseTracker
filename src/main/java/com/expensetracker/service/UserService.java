package com.expensetracker.service;

import com.expensetracker.dto.RegistrationResult;
import com.expensetracker.dto.UserDTO;
import com.expensetracker.entity.User;
import com.expensetracker.exception.*;
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


    // Injections
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CognitoService cognitoService;

    /**
     * Registers a new user in the system using AWS Cognito for authentication and stores the user information
     * in the local database. This method first validates the user data, then registers the user with Cognito,
     * and finally saves the user information in the database if registration is successful.
     *
     * @param userDTO The UserDTO object containing the user's data to be registered.
     * @return RegistrationResult An object containing the result of the registration process, including
     *                            the status and messages related to the process and the username for which
     *                            the registration was attempted.
     * @throws DataIntegrityViolationException If a database integrity constraint is violated.
     * @throws Exception If any other unexpected errors occur during the registration process.
     */
    @Transactional
    public RegistrationResult addUser(UserDTO userDTO) {
        validateUserData(userDTO);
        log.debug("Starting registration process for username: {}", userDTO.getUsername());

        try {
            String password = userDTO.getPassword();
            String cognitoUuid = cognitoService.registerUserWithCognito(userDTO.getUsername(), password, userDTO.getEmail());
            userDTO.setCognitoUuid(cognitoUuid);

            User user = mapDTOToUser(userDTO, new User());
            user.setStatus("Pending");  // Set user status as Pending until confirmed
            userRepository.save(user);
            return new RegistrationResult("SUCCESS", "User registered successfully for: " + userDTO.getUsername(), userDTO.getUsername());
        } catch (UserConfirmationRequiredException e) {
            log.info("User registration pending confirmation for username: {}", userDTO.getUsername());
            return new RegistrationResult("PENDING_CONFIRMATION", e.getMessage(), userDTO.getUsername());
        } catch (RegistrationException e) {
            log.error("Registration failed for user {}: {}", userDTO.getUsername(), e.getMessage());
            return new RegistrationResult("ERROR", e.getMessage(), userDTO.getUsername());
        } catch (Exception e) {
            log.error("Unexpected error during registration for user {}: {}", userDTO.getUsername(), e.getMessage());
            return new RegistrationResult("ERROR", "Unexpected error during user registration for: " + userDTO.getUsername(), userDTO.getUsername());
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
                    .filter(user -> "Active".equals(user.getStatus()))  // Check if user is active
                    .map(this::convertToDTO)
                    .orElseThrow(() -> new UserNotFoundException("User with UUID " + cognitoUuid + " not found in local database or is not active."));
        } catch (CognitoServiceException e) {
            throw new AuthenticationException("Cognito authentication failed: " + e.getMessage());
        }
    }



    /**
     * Validates a UserDTO object to ensure all required fields meet the necessary criteria for a user's data.
     * This includes non-null checks and specific format validations for username, email, and password.
     * It also checks the uniqueness of the username and email in the system to prevent duplicates.
     *
     * @param userDTO The UserDTO object containing the user data to validate.
     * @throws IllegalArgumentException if any validation fails. The exception message details the specific reason for the failure.
     */
    private void validateUserData(UserDTO userDTO) {
        if (userDTO == null) {
            throw new IllegalArgumentException("User data cannot be null");
        }

        // Validate username
        if (userDTO.getUsername() == null || userDTO.getUsername().trim().isEmpty()) {
            throw new InvalidUsernameException("Username is required and cannot be blank");
        }

        // Validate email format
        if (userDTO.getEmail() == null || !isValidEmail(userDTO.getEmail())) {
            throw new InvalidEmailException("Invalid email address");
        }

        // Validate password
        if (userDTO.getPassword() == null || !isValidPassword(userDTO.getPassword())) {
            throw new IllegalArgumentException("Password must meet complexity requirements");
        }

        // Example: Check if username or email already exists
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new UsernameAlreadyExistsException("Username is already taken");
        }

        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new EmailAlreadyExistsException("Email is already in use");
        }

    }

    /**
     * Validates an email address against a specified regex pattern. This method checks if the provided email
     * is not null and conforms to a standard email format, which includes having characters before and after an "@",
     * followed by a domain with a 2 to 4 character top-level domain.
     *
     * @param email The email address to be validated.
     * @return true if the email address is valid according to the regex pattern, false otherwise.
     */
    private boolean isValidEmail(String email) {
        String emailRegex = "^[\\w-\\.]+@[\\w-]+\\.[a-z]{2,4}$";
        return email != null && email.matches(emailRegex);
    }


    /**
     * Validates a password based on certain criteria encapsulated in a regex pattern. This pattern checks that the
     * password is at least 8 characters long and contains at least one digit and one special character. The method
     * ensures that the password is not null and matches this regex.
     *
     * @param password The password string to validate.
     * @return true if the password meets the criteria, false otherwise.
     */
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
     * Retrieves all users.
     *
     * @return a list of all users as DTOs.
     */
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(this::convertToDTO).collect(Collectors.toList());
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


    /**
     * Activates a user based on the Cognito UUID provided. This method searches for the user in the repository
     * by their Cognito UUID. If the user is found, it sets their status to "Active" and saves the updated user
     * back to the repository. The method returns true if the user is successfully activated, or false if no user
     * with the given UUID could be found.
     *
     * @param cognitoUuid The Cognito UUID of the user to be activated.
     * @return true if the user is successfully activated, false otherwise.
     */
    public boolean activateUser(String cognitoUuid) {
        return userRepository.findByCognitoUuid(cognitoUuid)
                .map(user -> {
                    user.setStatus("Active");
                    userRepository.save(user);
                    return true;
                }).orElse(false);
    }


    /**
     * Retrieves a UserDTO object by extracting the username from a RegistrationResult object. This method
     * ensures that both the RegistrationResult object and its username are not null. If they are, it throws
     * an IllegalArgumentException. If the conditions are met, it fetches the user data transfer object (DTO)
     * based on the username found in the RegistrationResult.
     *
     * @param result The RegistrationResult object which contains the user's registration details.
     * @return UserDTO The user data transfer object corresponding to the username in the RegistrationResult.
     * @throws IllegalArgumentException If the RegistrationResult object or the username within it is null.
     */
    public UserDTO fetchUserDTOFromResult(RegistrationResult result) {
        if (result == null || result.getUsername() == null) {
            throw new IllegalArgumentException("Registration result or username cannot be null.");
        }
        return findByUsername(result.getUsername());
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
                user.isActive(),
                user.getStatus()
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






}
