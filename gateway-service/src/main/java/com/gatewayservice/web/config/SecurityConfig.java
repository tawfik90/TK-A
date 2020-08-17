package com.gatewayservice.web.config;

import com.gatewayservice.exception.RestAccessDeniedHandler;
import com.gatewayservice.exception.RestAuthenticationEntryPoint;
import com.gatewayservice.web.filter.SecurityFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * SecurityConfig class that extends WebSecurityConfigurerAdapter and
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    final private SecurityFilter securityFilter;
    final private RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    final private RestAccessDeniedHandler restAccessDeniedHandler;

    /**
     * Dependency injection default constructor
     *
     * @param securityFilter               {@link SecurityFilter}
     * @param restAuthenticationEntryPoint {@link RestAuthenticationEntryPoint}
     * @param restAccessDeniedHandler      {@link RestAccessDeniedHandler}
     */
    public SecurityConfig(SecurityFilter securityFilter, RestAuthenticationEntryPoint restAuthenticationEntryPoint, RestAccessDeniedHandler restAccessDeniedHandler) {
        this.securityFilter = securityFilter;
        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
        this.restAccessDeniedHandler = restAccessDeniedHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(restAuthenticationEntryPoint)
                .accessDeniedHandler(restAccessDeniedHandler)
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
