package com.expensetracker.controller;


import com.expensetracker.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;
import com.expensetracker.service.*;

@CrossOrigin
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;


    @GetMapping("/success")
    public ResponseEntity<String> loginSuccess() {
        return ResponseEntity.ok("Successfully authenticated with Cognito!");
    }

    @GetMapping("/profile")
    public ResponseEntity<UserDTO> getCurrentUserProfile(@AuthenticationPrincipal OidcUser oidcUser) {
        String cognitoUserId = oidcUser.getSubject(); // Get the unique user ID from Cognito
        UserDTO user = userService.getUserByCognitoId(cognitoUserId);
        return ResponseEntity.ok(user);
    }
}