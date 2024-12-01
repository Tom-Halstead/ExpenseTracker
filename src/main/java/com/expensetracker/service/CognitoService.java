package com.expensetracker.service;

import com.expensetracker.exception.AuthenticationException;
import com.expensetracker.exception.RegistrationException;
import com.expensetracker.exception.ServiceException;
import com.expensetracker.exception.UserConfirmationRequiredException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

import java.util.Map;

@Service
public class CognitoService {

    private final CognitoIdentityProviderClient cognitoClient;

    private static final Logger log = LoggerFactory.getLogger(CognitoService.class);

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
            } else {
//                log.warn("No authentication result for user: {}", username);
                throw new AuthenticationException("Authentication failed or no result returned.");
            }
        } catch (CognitoIdentityProviderException e) {
//            log.error("AWS Cognito error during authentication for user {}: {}", username, e.awsErrorDetails().errorMessage());
            throw new ServiceException("Cognito service error: " + e.awsErrorDetails().errorMessage(), e);
        } catch (Exception e) {
//            log.error("Exception during authentication for user {}: {}", username, e.getMessage());
            throw new ServiceException("An unexpected error occurred during authentication.", e);
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
        final String clientId = System.getenv("COGNITO_CLIENT_ID");
        try {
            SignUpRequest signUpRequest = SignUpRequest.builder()
                    .clientId(clientId)
                    .username(username)
                    .password(password)
                    .userAttributes(AttributeType.builder().name("email").value(email).build())
                    .build();

            SignUpResponse response = cognitoClient.signUp(signUpRequest);
            return response.userSub(); // Returns the UUID from Cognito
        } catch (CognitoIdentityProviderException e) {
            log.error("AWS Cognito exception occurred for user {}: {}", username, e.awsErrorDetails().errorMessage());
            throw new RegistrationException("Error with AWS Cognito service: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Exception occurred during Cognito registration for user {}: {}", username, e.getMessage());
            throw new RegistrationException("Unexpected error during registration", e);
        }
    }

}

