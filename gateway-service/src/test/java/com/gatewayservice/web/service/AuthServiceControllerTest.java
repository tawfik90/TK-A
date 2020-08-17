package com.gatewayservice.web.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.xml.bind.DatatypeConverter;

import static org.junit.jupiter.api.Assertions.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthServiceControllerTest {
    @LocalServerPort
    private int port;

    TestRestTemplate restTemplate = new TestRestTemplate();

    HttpHeaders headers = new HttpHeaders();

    @Test
    public void testUnauthorized_NoHeader() throws Exception {
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/accesstoken"), HttpMethod.GET,
                entity, String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void testUnAuthorized_WrongPassword() throws Exception {
        String s1 = "tk-username:password";
        String encodeds1 = DatatypeConverter.printBase64Binary(s1.getBytes());
        headers.add("Authorization", "Basic " + encodeds1);
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/accesstoken"), HttpMethod.GET,
                entity, String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void testUnAuthorized_WrongUserName() throws Exception {
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        Mockito.doReturn(null).when(securityContext).getAuthentication();
        String s1 = "any:tk-password";
        String encodeds1 = DatatypeConverter.printBase64Binary(s1.getBytes());
        headers.add("Authorization", "Basic " + encodeds1);
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/accesstoken"), HttpMethod.GET,
                entity, String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void testAuthorized() throws Exception {
        String s1 = "tk-username:tk-password";
        String encodeds1 = DatatypeConverter.printBase64Binary(s1.getBytes());
        headers.add("Authorization", "Basic " + encodeds1);
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/accesstoken"), HttpMethod.GET,
                entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}