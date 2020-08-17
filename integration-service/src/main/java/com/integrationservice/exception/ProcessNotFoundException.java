package com.integrationservice.exception;

/**
 * ProcessNotFoundException that is used in the retrieve not exist process_id
 */
public class ProcessNotFoundException extends RuntimeException {
    public ProcessNotFoundException(String message) {
        super(message);
    }
}
