package com.gatewayservice.web.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gatewayservice.business.service.JwtService;
import com.gatewayservice.business.service.MyUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;

/**
 * SecurityFilter Class that that handles all requests by checking for basic authentication header or token is exist
 * and validate the use depend on type of authentication.
 * if request going to /accesstoken it should provide basic authentication otherwise should provide the token
 */
@Slf4j
@Component
public class SecurityFilter extends OncePerRequestFilter {

    final private JwtService jwtService;
    final private MyUserDetailsService userDetailsService;
    final private ObjectMapper objectMapper;

    public SecurityFilter(JwtService jwtService, MyUserDetailsService userDetailsService, ObjectMapper objectMapper) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        log.info("Entered SecurityFilter and starting validate the authentication");
        final String authenticationHeader = request.getHeader("Authorization");
        String jwt;
        String username = null;
        char[] password = null;
        if (authenticationHeader != null) {
            if (authenticationHeader.startsWith("Bearer ") && !request.getRequestURI().equals("/accesstoken")) {
                log.info("Request has bearer token");
                jwt = authenticationHeader.substring(7);
                username = jwtService.extractUsername(jwt);
            } else if (authenticationHeader.startsWith("Basic ") && request.getRequestURI().equals("/accesstoken")) {
                log.info("Request has basic authentication");
                byte[] decodedBytes = DatatypeConverter.parseBase64Binary(authenticationHeader.substring(5));
                username = new String(decodedBytes).split(":")[0];
                password = new String(decodedBytes).split(":")[1].toCharArray();
            }
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            log.info("Check the authority of user {}", username);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (userDetails == null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid username or password");
                return;
            }
            if (password != null) {
                if (!userDetails.getPassword().equals(String.valueOf(password))) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid username or password");
                    return;
                }
            }
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails,
                            password != null ? String.valueOf(password) : null,
                            userDetails.getAuthorities());
            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            log.info("User {} has permission to access", username);
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }
        filterChain.doFilter(request, response);
    }
}
