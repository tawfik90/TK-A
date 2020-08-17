package com.gatewayservice.web.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gatewayservice.business.service.JwtService;
import com.gatewayservice.business.service.MyUserDetailsService;
import org.junit.Before;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class SecurityFilterTest {

    private JwtService jwtService;
    private MyUserDetailsService userDetailsService;
    private HttpServletRequest request;
    private FilterChain filterChain;
    private HttpServletResponse response;
    private Authentication authentication;
    private UserDetails userDetails;
    private ObjectMapper objectMapper;

    private SecurityFilter sf;

    @Before
    public void initObj() {
        jwtService = Mockito.mock(JwtService.class);
        userDetailsService = Mockito.mock(MyUserDetailsService.class);
        request = Mockito.mock(HttpServletRequest.class);
        filterChain = Mockito.mock(FilterChain.class);
        response = Mockito.mock(HttpServletResponse.class);
        authentication = Mockito.mock(Authentication.class);
        userDetails = Mockito.mock(UserDetails.class);
        objectMapper = Mockito.mock(ObjectMapper.class);

        sf = new SecurityFilter(jwtService, userDetailsService, objectMapper);
    }

    @Test
    public void doFilterInternal_HeaderNullTest() throws IOException, ServletException {
        Mockito.doReturn("Foo").when(jwtService).extractUsername(Mockito.anyString());
        Mockito.doReturn(null).when(request).getHeader("Authorization");
        Mockito.doNothing().when(filterChain).doFilter(request, response);
        sf.doFilterInternal(request, response, filterChain);
        verify(filterChain, times(1)).doFilter(request, response);
        verify(jwtService, never()).extractUsername(Mockito.anyString());
    }

    @Test
    public void doFilterInternal_HeaderStartWithBearerAndURIContainsAccessTokenTest()
            throws IOException, ServletException {
        Mockito.doReturn("Foo").when(jwtService).extractUsername(Mockito.anyString());
        Mockito.doReturn("Bearer anything").when(request).getHeader("Authorization");
        Mockito.doReturn("/accesstoken").when(request).getRequestURI();
        Mockito.doNothing().when(filterChain).doFilter(request, response);
        sf.doFilterInternal(request, response, filterChain);
        verify(filterChain, times(1)).doFilter(request, response);
        verify(jwtService, never()).extractUsername(Mockito.anyString());
    }

    @Test
    public void doFilterInternal_HeaderStartWithBearerAndURINotAccessTokenTest() throws IOException, ServletException {
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.doReturn(authentication).when(securityContext).getAuthentication();
        Mockito.doReturn("Foo").when(jwtService).extractUsername(Mockito.anyString());
        Mockito.doReturn("Bearer anything").when(request).getHeader("Authorization");
        Mockito.doReturn("/anyUrl").when(request).getRequestURI();
        Mockito.doReturn(userDetails).when(userDetailsService).loadUserByUsername("Foo");
        Mockito.doReturn(null).when(userDetails).getPassword();
        Mockito.doNothing().when(filterChain).doFilter(request, response);
        sf.doFilterInternal(request, response, filterChain);
        verify(filterChain, times(1)).doFilter(request, response);
        verify(jwtService, times(1)).extractUsername(Mockito.anyString());
    }

    @Test
    public void doFilterInternal_NullAuthentication() throws IOException, ServletException {
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        Mockito.doReturn(null).when(securityContext).getAuthentication();
        Mockito.doReturn("Foo").when(jwtService).extractUsername(Mockito.anyString());
        Mockito.doReturn("Bearer anything").when(request).getHeader("Authorization");
        Mockito.doReturn("/anyUrl").when(request).getRequestURI();
        Mockito.doReturn(userDetails).when(userDetailsService).loadUserByUsername("Foo");
        Mockito.doReturn("pass").when(userDetails).getPassword();
        Mockito.doNothing().when(filterChain).doFilter(request, response);
        sf.doFilterInternal(request, response, filterChain);
        verify(filterChain, times(1)).doFilter(request, response);
        verify(jwtService, times(1)).extractUsername(Mockito.anyString());
    }

    @Test
    public void doFilterInternal_IncorrectPassword() throws IOException, ServletException {
        String s1 = "Foo:password";
        String encodeds1 = DatatypeConverter.printBase64Binary(s1.getBytes());
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        Mockito.doReturn(null).when(securityContext).getAuthentication();
        Mockito.doReturn("Foo").when(jwtService).extractUsername(Mockito.anyString());
        Mockito.doReturn("Basic " + encodeds1).when(request).getHeader("Authorization");
        Mockito.doReturn("/accesstoken").when(request).getRequestURI();
        Mockito.doReturn(userDetails).when(userDetailsService).loadUserByUsername("Foo");
        Mockito.doReturn("password").when(userDetails).getPassword();
        Mockito.doNothing().when(filterChain).doFilter(request, response);
        sf.doFilterInternal(request, response, filterChain);
        verify(filterChain, times(1)).doFilter(request, response);
    }

}