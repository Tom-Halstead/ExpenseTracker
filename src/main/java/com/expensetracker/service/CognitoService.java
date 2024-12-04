package com.expensetracker.service;

import com.expensetracker.dto.AuthResponse;
import com.expensetracker.exception.AuthenticationException;
import com.expensetracker.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

import java.time.Instant;
import java.util.Map;

@Service
public class CognitoService {

    private final CognitoIdentityProviderClient cognitoClient;

    final String CLIENTID = System.getenv("COGNITO_CLIENT_ID");
    final String USERPOOLID = "us-east-2_mPBSzEEWc";

    private static final Logger log = LoggerFactory.getLogger(CognitoService.class);

    @Autowired
    public CognitoService(CognitoIdentityProviderClient cognitoClient) {
        this.cognitoClient = cognitoClient;
    }

    /**
     * Authenticates a user via AWS Cognito and returns the JWT ID token if successful.
     *
     * @param email The username of the user trying to log in.
     * @param password The password of the user.
     * @return The JWT ID token if authentication is successful, null otherwise.
     */
    public AuthResponse authenticate(String email, String password) {
        validateCognitoConfiguration();
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty.");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty.");
        }


        try {
            AdminInitiateAuthRequest authRequest = AdminInitiateAuthRequest.builder()
                    .clientId(CLIENTID)
                    .userPoolId(USERPOOLID)
                    .authFlow(AuthFlowType.ADMIN_USER_PASSWORD_AUTH)
                    .authParameters(Map.of("USERNAME", email, "PASSWORD", password))
                    .build();


            AdminInitiateAuthResponse authResponse = cognitoClient.adminInitiateAuth(authRequest);

            if (authResponse.authenticationResult() != null) {
                return new AuthResponse(
                        authResponse.authenticationResult().idToken(),
                        authResponse.authenticationResult().accessToken(),
                        authResponse.authenticationResult().refreshToken(),
                        Instant.now().plusSeconds(authResponse.authenticationResult().expiresIn())
                );
            } else {
                throw new AuthenticationException("Authentication failed. Please check your credentials.");
            }
        } catch (CognitoIdentityProviderException e) {
            throw new ServiceException("Cognito service error: " + e.awsErrorDetails().errorMessage(), e);
        } catch (Exception e) {
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
    try {
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .clientId(CLIENTID)
                .username(username)
                .password(password)
                .userAttributes(AttributeType.builder().name("email").value(email).build())
                .build();

        SignUpResponse response = cognitoClient.signUp(signUpRequest);
        return response.userSub(); // Returns the UUID from Cognito used for Auth
    } catch (UsernameExistsException e) {
        throw new ServiceException("User with the given email/username already exists.", e);
    } catch (CognitoIdentityProviderException e) {
        throw new ServiceException("Error with AWS Cognito service: " + e.awsErrorDetails().errorMessage(), e);
    } catch (Exception e) {
        throw new ServiceException("Unexpected error during registration", e);
    }
}


    public void validateCognitoConfiguration() {
        if (CLIENTID == null || CLIENTID.trim().isEmpty()) {
            throw new IllegalStateException("Cognito Client ID is not configured.");
        }
        if (USERPOOLID == null || USERPOOLID.trim().isEmpty()) {
            throw new IllegalStateException("Cognito User Pool ID is not configured.");
        }
    }

}

