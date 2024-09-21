package com.app.cv.exception;

public class AuthServiceException extends Exception {
    private static final long serialVersionUID = 1L;

    private String errorCode;

    public AuthServiceException(String message) {
        super(message);
        this.errorCode = "UNKNOWN_ERROR";
    }

    public AuthServiceException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public AuthServiceException(String message, Throwable cause, String errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
