package com.expensetracker.dto;

import java.time.Instant;

/**
 * A DTO for authentication response, encapsulating tokens and expiration details.
 */
public class AuthResponse {

    private String idToken;      // ID token, typically used for user authentication
    private String accessToken;  // Access token, for accessing protected resources
    private String refreshToken; // Refresh token, for obtaining new tokens
    private Instant expiresAt;   // Token expiration timestamp

    /**
     * Default constructor.
     */
    public AuthResponse() {
    }

    /**
     * Parameterized constructor for full initialization.
     *
     * @param idToken      The ID token.
     * @param accessToken  The access token.
     * @param refreshToken The refresh token.
     * @param expiresAt    The expiration timestamp.
     */
    public AuthResponse(String idToken, String accessToken, String refreshToken, Instant expiresAt) {
        this.idToken = idToken;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresAt = expiresAt;
    }

    // Getters and Setters
    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    /**
     * Checks if the token is expired based on the current time.
     *
     * @return true if the token is expired, false otherwise.
     */
    public boolean isExpired() {
        return Instant.now().isAfter(expiresAt);
    }

    @Override
    public String toString() {
        return "AuthResponse{" +
                "idToken='" + idToken + '\'' +
                ", accessToken='" + accessToken + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", expiresAt=" + expiresAt +
                '}';
    }
}
