package com.app.cv.exception;

public class InvalidUserException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private String errorCode;

    public InvalidUserException(String message) {
        super(message);
        this.errorCode = "UNKNOWN_ERROR";
    }

    public InvalidUserException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public InvalidUserException(String message, Throwable cause, String errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}