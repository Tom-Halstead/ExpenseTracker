package com.expensetracker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

import java.util.Map;

@Service
public class CognitoService {

    private final CognitoIdentityProviderClient cognitoClient;

    @Autowired
    public CognitoService(CognitoIdentityProviderClient cognitoClient) {
        this.cognitoClient = cognitoClient;
    }

    /**
     * Authenticates a user via AWS Cognito and returns the JWT ID token if successful.
     *
     * @param username The username of the user trying to log in.
     * @param password The password of the user.
     * @return The JWT ID token if authentication is successful, null otherwise.
     */
    public String authenticate(String username, String password) {
        try {
            AdminInitiateAuthRequest authRequest = AdminInitiateAuthRequest.builder()
                    .clientId("7mvfnfbhcnmko6r5u74si479r0") // Use your actual Cognito App client ID
                    .userPoolId("us-east-2_mPBSzEEWc") // Use your actual Cognito User Pool ID
                    .authFlow(AuthFlowType.ADMIN_USER_PASSWORD_AUTH)
                    .authParameters(Map.of("USERNAME", username, "PASSWORD", password))
                    .build();

            AdminInitiateAuthResponse authResponse = cognitoClient.adminInitiateAuth(authRequest);
            if (authResponse.authenticationResult() != null) {
                return authResponse.authenticationResult().idToken(); // Return the ID token
            }
            return null; // Authentication failed or did not return a result
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Proper error handling should be implemented
        }
    }

    /**
     * Registers a user in Amazon Cognito.
     *
     * @param username The username for the new user.
     * @param password The password for the new user.
     * @param email The email for the new user.
     * @return The unique Cognito UUID for the new user, or null if confirmation is needed.
     */
    public String registerUserWithCognito(String username, String password, String email) {
        try {
            SignUpRequest signUpRequest = SignUpRequest.builder()
                    .clientId("7mvfnfbhcnmko6r5u74si479r0") // Your actual Cognito client ID
                    .username(username)
                    .password(password)
                    .userAttributes(AttributeType.builder().name("email").value(email).build())
                    .build();

            SignUpResponse response = cognitoClient.signUp(signUpRequest);
            if (!response.userConfirmed()) {
                // Handling confirmation here or inform the user to confirm via email
                return null;
            }
            return response.userSub(); // Returns the UUID from Cognito
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Handle exceptions appropriately
        }
    }


}