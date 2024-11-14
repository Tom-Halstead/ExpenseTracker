package com.expensetracker.service;

import com.expensetracker.dto.UserDTO;
import com.expensetracker.model.User;
import com.expensetracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

    @Service
    public class UserService {

        @Autowired
        private UserRepository userRepository;
        @Autowired
        private PasswordEncoder passwordEncoder;

        public List<UserDTO> getAllUsers() {
            List<User> users = userRepository.findAll();
            return users.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        }

        public UserDTO getUserById(int id) {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            return convertToDTO(user);
        }

        public UserDTO addUser(UserDTO userDTO) {
            User user = new User();
            user.setUsername(userDTO.getUsername());
            user.setPassword(passwordEncoder.encode("defaultPassword"));
            user.setEmail(userDTO.getEmail());
            user.setFirstName(userDTO.getFirstName());
            user.setLastName(userDTO.getLastName());
            user.setActive(userDTO.isActive());
            user = userRepository.save(user);
            return convertToDTO(user);
        }

        public UserDTO updateUser(int id, UserDTO userDTO) {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            user.setEmail(userDTO.getEmail());
            user.setFirstName(userDTO.getFirstName());
            user.setLastName(userDTO.getLastName());
            user.setActive(userDTO.isActive());
            user = userRepository.save(user);
            return convertToDTO(user);
        }

        public void deleteUser(int id) {
            userRepository.deleteById(id);
        }

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
