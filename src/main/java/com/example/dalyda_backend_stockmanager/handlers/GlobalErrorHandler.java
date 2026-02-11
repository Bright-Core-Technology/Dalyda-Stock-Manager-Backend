package com.example.dalyda_backend_stockmanager.handlers;

import com.example.dalyda_backend_stockmanager.responses.GenericResponse;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<GenericResponse<?>> handleDuplicateKeyException(DuplicateKeyException exception) {
        var response = new GenericResponse<>(exception.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<GenericResponse<?>> handleResourceNotFoundException(ResourceNotFoundException exception) {
        var response = new GenericResponse<>(exception.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
