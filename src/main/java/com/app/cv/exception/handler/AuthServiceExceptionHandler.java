package com.app.cv.exception.handler;

import com.app.cv.exception.AuthServiceException;
import com.app.cv.exception.InvalidUserException;
import com.app.cv.exception.UserAlreadyExistException;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.http.MediaType;

@ControllerAdvice
public class AuthServiceExceptionHandler {

     // Create a logger instance for the class
    private static final Logger logger = LoggerFactory.getLogger(AuthServiceExceptionHandler.class);


    @ExceptionHandler(InvalidUserException.class)
    public ResponseEntity<ErrorResponse> handleInvalidUserException(InvalidUserException ex, WebRequest request) {
        logger.error("InvalidUserException occurred: {}", ex.getMessage(), ex);
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(errorResponse);
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistException(UserAlreadyExistException ex,  WebRequest request) {
        logger.error("UserAlreadyExistException occurred: {}", ex.getMessage(), ex);
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.ALREADY_REPORTED.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(errorResponse);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex, WebRequest request) {
        logger.error("AuthenticationException occurred: {}", ex.getMessage(), ex);
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(errorResponse);
    }

 

    @ExceptionHandler(AuthServiceException.class)
    public ResponseEntity<ErrorResponse> handleAuthServiceException(AuthServiceException ex, WebRequest request) {
        logger.error("AuthServiceException occurred: {}", ex.getMessage(), ex);
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(errorResponse);
    }

    class ErrorResponse {
        private String message;
        private LocalDateTime timestamp;
        private int status;

        public ErrorResponse( int status, String message) {
            this.message = message;
            this.status = status;
            this.timestamp = LocalDateTime.now();
        }

        // Getters and Setters
    }
}
