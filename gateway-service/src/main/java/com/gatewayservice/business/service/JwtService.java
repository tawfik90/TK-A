package com.gatewayservice.business.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * JWT Service Class for common Java Web Token operations
 */
@Slf4j
@Component
public class JwtService {
    private String SECRET_KEY = "secret";

    /**
     * Extracts username from the token
     *
     * @param token jwt String
     * @return username as String
     */
    public String extractUsername(String token) {
        log.info("Entered extractUsername(String) to extract the username of token {}", token);
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts expiration data from the token
     *
     * @param token jwt String
     * @return expiration date
     */
    public Date extractExpiration(String token) {
        log.info("Entered extractExpiration(String) to extract the expiration date of token {}", token);
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts specific claim from the token
     *
     * @param token          jwt String
     * @param claimsResolver {@link Function}
     * @param <T>            generic
     * @return claims as object
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extracts all claims
     *
     * @param token jwt String
     * @return claims
     */
    protected Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    /**
     * Checks the expiration of token
     *
     * @param token jwt String
     * @return true or false
     */
    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * creates new token
     *
     * @param userDetails
     * @return token as String
     */
    public String generateToken(UserDetails userDetails) {
        log.info("Entered generateToken(UserDetails) for username {}", userDetails.getUsername());
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    protected String createToken(Map<String, Object> claims, String subject) {
        log.info("Entered createToken(Map<String, Object>, subject)");
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }

    /**
     * Checks the token validation
     *
     * @param token
     * @param userDetails
     * @return true or false
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        log.info("Entered validateToken(String, UserDetails) to validate user and expiration date of token");
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

}
