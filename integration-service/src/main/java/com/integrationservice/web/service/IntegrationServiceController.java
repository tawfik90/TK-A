package com.integrationservice.web.service;

import com.integrationservice.business.service.IntegrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Integration Service controller class that responsible to submit the cv and
 * retrieve the processing status or processing result.
 * Integration Service controller class relies on {@link Environment} and {@link IntegrationService}
 */
@RestController
@Slf4j
public class IntegrationServiceController {

    private final IntegrationService integrationService;
    private final Environment environment;

    /**
     * Dependency injection default constructor
     *
     * @param integrationService {@link IntegrationService}
     * @param environment        {@link Environment}
     */
    public IntegrationServiceController(IntegrationService integrationService, Environment environment) {
        this.integrationService = integrationService;
        this.environment = environment;
    }

    /**
     * /submit end-point gets CV in a form of MultipartFile and returns process_id by passing the received
     * CV file to submitCV(MultipartFile) method in {@link IntegrationService}
     *
     * @param file
     * @return process_id in the body as Long
     * {@link com.integrationservice.business.model.response.ProcessResponse}
     * @throws IOException
     * @throws InterruptedException
     */
    @RequestMapping(value = "/submit", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> submitCV(@RequestParam("file") MultipartFile file) throws IOException, InterruptedException {
        log.info("Submit end-point called on port {}", environment.getProperty("local.server.port"));
        return ResponseEntity.ok(integrationService.submitCV(file));
    }

    /**
     * /retrieve/{processId} end-point retrieves process result or process status
     * by calling getExtractedResult(processId) in {@link IntegrationService}
     *
     * @param processId as Long
     * @return ProfileDTO or status otherwise
     * {@link com.integrationservice.business.model.response.ProcessResultResponse}
     */
    @RequestMapping(value = "/retrieve/{processId}", method = RequestMethod.GET)
    public ResponseEntity<?> getExtractedInformation(@PathVariable Long processId) {
        log.info("retrieve end-point called on port {}", environment.getProperty("local.server.port"));
        return ResponseEntity.ok(integrationService.getExtractedResult(processId));
    }
}
