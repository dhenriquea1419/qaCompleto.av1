package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler {

    private static final MediaType UTF8_JSON = MediaType.parseMediaType("application/json;charset=UTF-8");

    @ExceptionHandler(InvalidItemDataException.class)
    public ResponseEntity<String> handleInvalid(InvalidItemDataException ex) {
        return ResponseEntity.badRequest().contentType(UTF8_JSON).body(ex.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleNotFound(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(UTF8_JSON).body(ex.getMessage());
    }
}
