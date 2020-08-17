package com.integrationservice.business.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldNameConstants;

/**
 * data model that is used for building the response of the /submit end-point
 */

@Getter
@AllArgsConstructor
@FieldNameConstants()
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProcessResponse {

    @JsonProperty("process_id")
    private final Long processId;


}
