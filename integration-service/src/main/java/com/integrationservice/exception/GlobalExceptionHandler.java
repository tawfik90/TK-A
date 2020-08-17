package com.integrationservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Global Exception Handler that is used to handle exception
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handles the process_id not found exception response
     *
     * @param e {@link Exception}
     * @return custom error message {@link ExceptionResponse}
     */
    @ExceptionHandler(ProcessNotFoundException.class)
    public ResponseEntity processNotFoundHandle(Exception e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionResponse(e.getMessage(), HttpStatus.NOT_FOUND.value()));
    }

}
