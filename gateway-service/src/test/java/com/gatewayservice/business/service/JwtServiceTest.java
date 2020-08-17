package com.gatewayservice.business.service;

import com.gatewayservice.business.service.JwtService;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class JwtServiceTest {
    private Map<String, Object> claims;
    private JwtService jwts;
    private JwtService jwtsSpy;
    private UserDetails userDetails;

    @Before
    public void initObj() {

        claims = new HashMap<String, Object>();
        claims.put("id", "10");
        claims.put("last name", "boo");

        jwts = new JwtService();
        jwtsSpy = Mockito.spy(jwts);
        userDetails = Mockito.mock(UserDetails.class);

    }

    @Test
    public void generateToken_CallingCreateTokenTest() {
        Mockito.doReturn("foo").when(userDetails).getUsername();
        jwtsSpy.generateToken(userDetails);
        verify(jwtsSpy, times(1)).createToken(Mockito.any(), Mockito.anyString());
    }

    @Test
    public void createTokenTest() {
        Mockito.doReturn("foo").when(userDetails).getUsername();
        String token = jwtsSpy.generateToken(userDetails);
        assertTrue(jwtsSpy.validateToken(token, userDetails));
    }

    @Test
    public void isTokenExpiredTest() {
        Mockito.doReturn("foo").when(userDetails).getUsername();
        String token = jwtsSpy.generateToken(userDetails);
        assertTrue(!jwtsSpy.isTokenExpired(token));
    }
}