package com.integrationservice.business.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.integrationservice.business.model.ProfileDTO;
import com.integrationservice.business.model.response.ProcessResponse;
import com.integrationservice.business.model.response.ProcessResultResponse;
import com.integrationservice.data.entity.Process;
import com.integrationservice.data.repository.ProcessRepository;
import com.integrationservice.exception.ProcessNotFoundException;
import com.integrationservice.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

/**
 * Integration service class that responsible to provide all services for starting from submitting CV to
 * extracting cv result using repository to make operations on database and carrying the required business logic
 */
@Service
@Slf4j
public class IntegrationService {

    final private ProcessRepository processRepository;
    final private TKAsyncOperationService tkAsyncOperationService;
    final private ObjectMapper objectMapper;
    final private FileUtil fileUtil;

    /**
     * Dependency injection default constructor
     *
     * @param processRepository       {@link ProcessRepository}
     * @param tkAsyncOperationService {@link TKAsyncOperationService}
     * @param objectMapper            {@link ObjectMapper}
     * @param fileUtil                {@link FileUtil}
     */
    public IntegrationService(ProcessRepository processRepository,
                              TKAsyncOperationService tkAsyncOperationService,
                              ObjectMapper objectMapper,
                              FileUtil fileUtil) {
        this.processRepository = processRepository;
        this.tkAsyncOperationService = tkAsyncOperationService;
        this.objectMapper = objectMapper;
        this.fileUtil = fileUtil;
    }

    /**
     * Gets the CV and start processing and preparing to be passed to TK extract service
     * it calls async startProcessCV method in {@link TKAsyncOperationService}
     *
     * @param file representation of CV's file
     * @return {@link ProcessResponse}
     * @throws IOException to throw IO Exception
     */
    public ProcessResponse submitCV(MultipartFile file) throws IOException {
        log.info("Entered submitCV(MultipartFile) Method");
        Process process = new Process(new Date());
        process = processRepository.save(process);
        // calling Async method to perform the TK extract service
        tkAsyncOperationService.startProcessCV(fileUtil.getContentAsResource(file), process);
        log.info("Return generated process_id {} from submitCV", process.getId());
        return new ProcessResponse(process.getId());
    }

    /**
     * Gets processId to retrieve the extraction result as (Process Result) from database
     * if there is no result it returns PROGRESS status
     *
     * @param processId processId input
     * @return {@link ProcessResultResponse}
     */
    public ProcessResultResponse getExtractedResult(Long processId) {
        log.info("Entered getExtractedResult(String) Method");
        Optional<Process> process = processRepository.findById(processId);
        if (!process.isPresent()) {
            throw new ProcessNotFoundException("Process id not found");
        }
        log.info("Preparing to return process result response");
        return process
                .map(Process::getProcessResult)
                .map(v -> new ProcessResultResponse(null, getProfile(v.getResult())))
                .orElseGet(() -> new ProcessResultResponse("PROGRESS", null));
    }

    /**
     * gets Json String and parse it to ProfileDTO {@link ProfileDTO}
     *
     * @param jsonString profile in jason string input
     * @return {@link ProfileDTO}
     */
    private ProfileDTO getProfile(String jsonString) {
        try {
            log.info("Entered getProfile(String) to convert json String to ProfileDTO");
            return objectMapper.readValue(jsonString, ProfileDTO.class);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        return null;
    }

}
