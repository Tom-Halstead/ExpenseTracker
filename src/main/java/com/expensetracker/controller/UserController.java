package com.expensetracker.controller;

import com.expensetracker.dto.UserDTO;
import com.expensetracker.security.UserPrincipal;
import com.expensetracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Get all users (admin-only access)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // Get the current user's profile
    @GetMapping("/profile")
    public ResponseEntity<UserDTO> getCurrentUserProfile(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        UserDTO user = userService.getUserById(userPrincipal.getId());
        return ResponseEntity.ok(user);
    }

    // Get a specific user by ID (admin-only access)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable int id) {
        UserDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    // Register a new user
    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody UserDTO userDTO) {
        UserDTO newUser = userService.addUser(userDTO);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    // Update the current user's profile
    @PutMapping("/profile")
    public ResponseEntity<UserDTO> updateCurrentUserProfile(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                            @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateUser(userPrincipal.getId(), userDTO);
        return ResponseEntity.ok(updatedUser);
    }

    // Update another user by ID (admin-only access)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUserById(@PathVariable int id, @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateUser(id, userDTO);
        return ResponseEntity.ok(updatedUser);
    }

    // Delete a user by username or ID (admin-only access)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteUser(@RequestParam(required = false) String username,
                                           @RequestParam(required = false) Integer id) {
        if (username != null) {
            userService.deleteUserByUsername(username);
        } else if (id != null) {
            userService.deleteUserById(id);
        } else {
            return ResponseEntity.badRequest().build(); // Return 400 if neither is provided
        }
        return ResponseEntity.noContent().build();
    }
}
