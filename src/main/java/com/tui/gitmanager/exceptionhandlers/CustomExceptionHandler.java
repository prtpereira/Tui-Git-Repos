package com.tui.gitmanager.exceptionhandlers;

import com.tui.gitmanager.exceptions.UsernameNotFoundException;
import com.tui.gitmanager.representers.CustomExceptionRepresenter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<CustomExceptionRepresenter> handleUsernameNotFound(UsernameNotFoundException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new CustomExceptionRepresenter(404, ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomExceptionRepresenter> handleDefaultException(Exception ex) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new CustomExceptionRepresenter(500, ex.getMessage()));
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<Object> handleMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex) {
        String errorMessage = "{\n" +
                "    \"status\": 406,\n" +
                "    \"message\": \"Unsupported media type. Only 'application/json' is supported.\"\n" +
                "}";

        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errorMessage);
    }
}