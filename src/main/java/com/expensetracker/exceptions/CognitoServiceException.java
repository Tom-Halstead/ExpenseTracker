package com.expensetracker.exceptions;

public class CognitoServiceException extends RuntimeException {
    public CognitoServiceException() {
        super();
    }

    public CognitoServiceException(String message) {
        super(message);
    }

    public CognitoServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public CognitoServiceException(Throwable cause) {
        super(cause);
    }

}
