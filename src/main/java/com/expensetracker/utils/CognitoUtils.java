package com.expensetracker.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;

public class CognitoUtils {

    /**
     * Extracts the Cognito UUID (user identifier) from the provided ID token.
     * The UUID is typically stored in the "sub" (subject) claim of the JWT token.
     *
     * @param idToken The ID token returned by AWS Cognito after successful authentication.
     * @return The Cognito UUID (user identifier) extracted from the token.
     * @throws IllegalArgumentException if the ID token is null, empty, or does not contain the "sub" claim.
     */
    public static String extractCognitoUuid(String idToken) {
        if (idToken == null || idToken.trim().isEmpty()) {
            throw new IllegalArgumentException("ID token cannot be null or empty.");
        }

        try {
            // Decode the JWT token using a library like Auth0's Java JWT
            DecodedJWT decodedJWT = JWT.decode(idToken);

            // Extract the "sub" claim, which usually contains the user UUID
            String uuid = decodedJWT.getClaim("sub").asString();

            if (uuid == null || uuid.trim().isEmpty()) {
                throw new IllegalArgumentException("The ID token does not contain a valid 'sub' claim.");
            }

            return uuid;
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to extract UUID from ID token. Ensure the token is valid.", e);
        }
    }

}

