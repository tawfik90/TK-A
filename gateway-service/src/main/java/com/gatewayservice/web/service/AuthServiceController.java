package com.gatewayservice.web.service;

import com.gatewayservice.business.service.JwtService;
import com.gatewayservice.business.model.AccessTokenResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * AuthServiceController API that responsible to check the credentials
 * and returns access token upon successful authentication
 */
@Slf4j
@RestController
public class AuthServiceController {

    final private JwtService jwtService;

    /**
     * Dependency injection default constructor
     *
     * @param jwtService {@link JwtService}
     */
    public AuthServiceController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    /**
     * /accesstoken end-point returns token upon successful authentication by calling JwtService {@link JwtService}
     *
     * @param auth {@link Authentication}
     * @return AccessTokenResponse {@link AccessTokenResponse}
     */
    @RequestMapping(value = "/accesstoken", method = RequestMethod.GET)
    public ResponseEntity<?> getAccessToken(Authentication auth) {
        log.info("Entered /accesstoken end-point to get new accesstokon for usename {}", auth.getName());
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        return ResponseEntity.ok(new AccessTokenResponse(jwtService.generateToken(userDetails)));
    }

}
