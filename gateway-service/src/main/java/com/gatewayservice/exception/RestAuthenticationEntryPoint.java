package com.gatewayservice.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Exception Handler Class that Implements AuthenticationEntryPoint{@link AuthenticationEntryPoint}
 */
@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    final private ObjectMapper objectMapper;
    final private static String ERROR_MESSAGE_ATTRIBUTE_NAME = "javax.servlet.error.message";

    public RestAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        OutputStream out = response.getOutputStream();
        String message = "Unauthorized";
        response.setContentType(MediaType.APPLICATION_JSON);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        if (request.getAttribute(ERROR_MESSAGE_ATTRIBUTE_NAME) != null &&
                !request.getAttribute(ERROR_MESSAGE_ATTRIBUTE_NAME).equals("")) {
            message = request.getAttribute(ERROR_MESSAGE_ATTRIBUTE_NAME).toString();
        }
        objectMapper.writeValue(out, new ExceptionResponse(message, HttpServletResponse.SC_UNAUTHORIZED));
        out.flush();
    }
}
