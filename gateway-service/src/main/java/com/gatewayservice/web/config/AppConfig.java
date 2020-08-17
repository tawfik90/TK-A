package com.gatewayservice.web.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;

/**
 * AppConfig to create the common beans that is used within the entire service
 */
public class AppConfig {

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
