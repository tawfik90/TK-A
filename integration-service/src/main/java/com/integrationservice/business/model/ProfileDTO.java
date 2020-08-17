package com.integrationservice.business.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Map;

/**
 * data model used to create ProfileDTO that is used to carry the CV extracted result
 */
@Data
public class ProfileDTO {

    @JsonProperty("FirstName")
    private String firstName;
    @JsonProperty("LastName")
    private String lastName;
    @JsonProperty("Address")
    private Map<String, String> address;


}
