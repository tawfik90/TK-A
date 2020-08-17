package com.gatewayservice.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Exception Handler Class that Implement AccessDeniedHandler {@link AccessDeniedHandler}
 */
@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler {

    final private ObjectMapper objectMapper;

    /**
     * Dependency injection default constructor
     *
     * @param objectMapper {@link ObjectMapper}
     */
    public RestAccessDeniedHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        OutputStream out = httpServletResponse.getOutputStream();
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON);
        objectMapper.writeValue(out, new ExceptionResponse("Access Denied", HttpServletResponse.SC_FORBIDDEN));
        out.flush();
    }
}
