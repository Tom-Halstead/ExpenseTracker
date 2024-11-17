package com.expensetracker.controller;

import com.expensetracker.dto.UserDTO;
import com.expensetracker.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/users")
@Validated
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        logger.info("Fetching all users - ADMIN access");
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/profile")
    public ResponseEntity<UserDTO> getCurrentUserProfile(@AuthenticationPrincipal OidcUser oidcUser) {
        String cognitoUserId = oidcUser.getSubject();
        logger.info("Fetching profile for Cognito user ID: {}", cognitoUserId);
        UserDTO user = userService.getUserByCognitoId(cognitoUserId);
        return ResponseEntity.ok(user);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable int id) {
        logger.info("Fetching user by ID: {}", id);
        UserDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/profile")
    public ResponseEntity<UserDTO> updateCurrentUserProfile(@AuthenticationPrincipal OidcUser oidcUser,
                                                            @Valid @RequestBody UserDTO userDTO) {
        String cognitoUserId = oidcUser.getSubject();
        logger.info("Updating profile for Cognito user ID: {}", cognitoUserId);
        UserDTO updatedUser = userService.updateUserByCognitoId(cognitoUserId, userDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUserById(@PathVariable int id, @Valid @RequestBody UserDTO userDTO) {
        logger.info("Updating user by ID: {}", id);
        UserDTO updatedUser = userService.updateUser(id, userDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteUser(@RequestParam(required = false) String username,
                                           @RequestParam(required = false) Integer id) {
        if (username != null) {
            logger.info("Deleting user by username: {}", username);
            userService.deleteUserByUsername(username);
        } else if (id != null) {
            logger.info("Deleting user by ID: {}", id);
            userService.deleteUserById(id);
        } else {
            logger.warn("Bad request - must specify either username or ID to delete");
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.noContent().build();
    }
}