package com.takehome.stayease.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
//import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDenied(AccessDeniedException ex) {
    return buildErrorResponse("Access Denied: You don't have permission to perform this action",
    HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleBadCredentials(BadCredentialsException ex) {
        return buildErrorResponse("Invalid email or password", HttpStatus.NOT_FOUND); // 404
    }

    // Handle validation errors or bad inputs
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationErrors(MethodArgumentNotValidException ex) {
        String errorMsg = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return buildErrorResponse(errorMsg, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFound(ResourceNotFoundException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AvailableRoomsExceededException.class)
    public ResponseEntity<Object> handleAvailableRoomExceeded(AvailableRoomsExceededException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex) {
        return buildErrorResponse("Something went wrong: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneralException(Exception ex) {
        return buildErrorResponse("Something went wrong: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Utility method for building consistent response
    private ResponseEntity<Object> buildErrorResponse(String message, HttpStatus status) {
        Map<String, Object> errorBody = new LinkedHashMap<>();
        errorBody.put("message", message);
        errorBody.put("httpStatus", status.name());
        errorBody.put("localDateTime", LocalDateTime.now());
        return new ResponseEntity<>(errorBody, status);
    }
    
}
