package com.integrationservice.business.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.integrationservice.business.model.ProfileDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * data model that is used for building the response of the /retrieve/{processId} end-point
 */
@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProcessResultResponse {

    private final String status;
    private final ProfileDTO profile;


}
