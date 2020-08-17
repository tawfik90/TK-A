package com.gatewayservice.business.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * MyUserDetailsService Class that implements UserDetailsService to override loadUserByUsername(String)
 * method that is used to load user from map if it is exist (note you can use database)
 */
@Slf4j
@Service
public class MyUserDetailsService implements UserDetailsService {


    final private JwtService jwtService;

    /**
     * Dependency injection default constructor
     *
     * @param jwtService {@link JwtService}
     */
    public MyUserDetailsService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    /**
     * returns user by username
     *
     * @param username
     * @return User {@link User}
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Entered loadUserByUsername(String) get user by username {}", username);
        Map<String, String> users = new HashMap() {{
            put("tk-username", "tk-password");
        }};
        if (users.get(username) == null) {
            log.warn("This username {} is not exist", username);
            return null;
        }
        return new User(username, users.get(username), new ArrayList<>());
    }
}
