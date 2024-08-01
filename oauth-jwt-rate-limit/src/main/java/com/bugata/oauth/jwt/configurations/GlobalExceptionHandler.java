package com.bugata.oauth.jwt.configurations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.jsonwebtoken.ExpiredJwtException;
/**
 * @author Bugata
 * Created By Krishna on 01-07-2024
 * @project oauth-jwt
 */


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<String> handleExpiredJwtException(ExpiredJwtException ex) {
        // Log the exception (You can log it here)
        System.out.println("JWT expired: " + ex.getMessage());

        // Return an appropriate HTTP response
        return ResponseEntity.status(401).body("JWT expired. Please refresh token.");
    }

    // Add more exception handlers as needed for other exceptions
}