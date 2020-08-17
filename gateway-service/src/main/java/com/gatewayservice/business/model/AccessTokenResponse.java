package com.gatewayservice.business.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Data model that is used for building the response of the /accesstoken end-point
 */
@Getter
@AllArgsConstructor
public class AccessTokenResponse {

    private final String accessToken;

}
