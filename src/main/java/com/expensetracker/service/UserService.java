package com.expensetracker.service;

import com.expensetracker.dto.UserDTO;
import com.expensetracker.entity.User;
import com.expensetracker.exceptions.UserNotFoundException;
import com.expensetracker.exceptions.UsernameNotFoundException;
import com.expensetracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Retrieves all users.
     *
     * @return a list of all users
     */
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public UserDTO getUserByCognitoId(String cognitoId) {
        User user = userRepository.findByCognitoId(cognitoId)
                .orElseThrow(() -> new UserNotFoundException(Integer.parseInt(cognitoId)));
        return convertToDTO(user);
    }

    /**
     * Retrieves a user by ID.
     *
     * @param id the user ID
     * @return the user DTO
     */
    public UserDTO getUserById(int id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return convertToDTO(user);
    }

    /**
     * Retrieves a user by username.
     *
     * @param username the username
     * @return the user DTO, or null if not found
     */
    public UserDTO getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(this::convertToDTO)
                .orElse(null);
    }

    /**
     * Adds a new user to the database.
     * (Note: User authentication and password management are handled by Cognito.)
     *
     * @param userDTO the user data transfer object
     * @return the created user DTO
     */
    public UserDTO addUser(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setActive(userDTO.isActive());
        user = userRepository.save(user);
        return convertToDTO(user);
    }

    /**
     * Updates an existing user.
     *
     * @param id the user ID
     * @param userDTO the updated user data
     * @return the updated user DTO
     */
    public UserDTO updateUser(int id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        user.setEmail(userDTO.getEmail());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setActive(userDTO.isActive());
        user = userRepository.save(user);
        return convertToDTO(user);
    }

    public UserDTO updateUserByCognitoId(String cognitoId, UserDTO userDTO) {
        User user = userRepository.findByCognitoId(cognitoId)
                .orElseThrow(() -> new UserNotFoundException(Integer.parseInt(cognitoId)));
        user.setEmail(userDTO.getEmail());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setActive(userDTO.isActive());
        user = userRepository.save(user);
        return convertToDTO(user);
    }

    /**
     * Deletes a user by ID.
     *
     * @param id the user ID
     */
    public void deleteUserById(int id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        userRepository.delete(user);
    }

    /**
     * Deletes a user by username.
     *
     * @param username the username
     */
    public void deleteUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        userRepository.delete(user);
    }

    /**
     * Converts a User entity to a UserDTO.
     *
     * @param user the user entity
     * @return the user DTO
     */
    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setActive(user.isActive());
        return dto;
    }
}