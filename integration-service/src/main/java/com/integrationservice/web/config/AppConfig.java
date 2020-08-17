package com.integrationservice.web.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * AppConfig to create the common beans that is used within the entire service
 */
@Data
@NoArgsConstructor
@Configuration
public class AppConfig {

    /**
     * create singleton pattern for RestTemplate object (the client that is used to connect to external end-point)
     *
     * @return {@link RestTemplate}
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * create singleton pattern for ObjectMapper object (The Jackson API that is used to map between json and Objects)
     *
     * @return
     */
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}
