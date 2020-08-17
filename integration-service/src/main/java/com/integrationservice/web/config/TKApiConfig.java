package com.integrationservice.web.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * data model that is used to represent the connection with TK extract external service
 */
@Data
@NoArgsConstructor
@Component
public class TKApiConfig {

    @Value("${tk-extract-service.api.url}")
    private String url;

    @Value("${tk-extract-service.api.access.account}")
    private String account;

    @Value("${tk-extract-service.api.access.username}")
    private String username;

    @Value("${tk-extract-service.api.access.password}")
    private String password;

}
