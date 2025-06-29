package com.wanafiq.ecdsa.web.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<?> handleUnauthorizedException(Throwable ignored) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Unauthorized");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(response);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(Throwable ignored) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Not Found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    @ExceptionHandler
    public ResponseEntity<?> handleAnyException(Throwable ignored) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Internal Server Error");
        return ResponseEntity.internalServerError()
                .body(response);
    }

}
