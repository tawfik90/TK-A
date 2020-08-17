package com.integrationservice.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * Data model to carry the custom message exception response
 */
@Data
@Getter
@AllArgsConstructor
public class ExceptionResponse {

    private String message;
    private int status;

}
