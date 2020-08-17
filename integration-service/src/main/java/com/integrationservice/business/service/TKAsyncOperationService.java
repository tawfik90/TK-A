package com.integrationservice.business.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.integrationservice.business.model.ProfileDTO;
import com.integrationservice.data.entity.ProcessResult;
import com.integrationservice.data.repository.ProcessResultRepository;
import com.integrationservice.web.config.TKFormDataKeysConfig;
import com.integrationservice.data.entity.Process;
import com.integrationservice.web.config.TKApiConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

/**
 * Tk Async operation Service that responsible for calling TK extract external service
 * and retrieving the extract data then store it on the database
 */
@Service
@Slf4j
public class TKAsyncOperationService {

    final private RestTemplate restTemplate;
    final private TKApiConfig tkApiConfig;
    final private ProcessResultRepository processResultRepository;
    final private ObjectMapper objectMapper;

    /**
     * Dependency injection default constructor
     *
     * @param restTemplate            {@link RestTemplate}
     * @param tkApiConfig             {@link TKApiConfig}
     * @param processResultRepository {@link ProcessResultRepository}
     * @param objectMapper            {@link ObjectMapper}
     */
    public TKAsyncOperationService(RestTemplate restTemplate,
                                   TKApiConfig tkApiConfig,
                                   ProcessResultRepository processResultRepository,
                                   ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.tkApiConfig = tkApiConfig;
        this.processResultRepository = processResultRepository;
        this.objectMapper = objectMapper;
    }

    /**
     * async method that gets the file as ByteArrayResource and process {@link Process} to call TK extract external service.
     * Store the extract data in the database
     *
     * @param file    {@link ByteArrayResource}
     * @param process {@link Process}
     * @throws IOException throw io exception
     */
    @Async
    public void startProcessCV(ByteArrayResource file, Process process) throws IOException {
        log.info("Entered startProcessCV(MultipartFile, Process) Start processing and call TK-Service passing processId{} ",
                process.getId());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add(TKFormDataKeysConfig.ACCOUNT.getKey(), tkApiConfig.getAccount());
        body.add(TKFormDataKeysConfig.USERNAME.getKey(), tkApiConfig.getUsername());
        body.add(TKFormDataKeysConfig.PASSWORD.getKey(), tkApiConfig.getPassword());
        body.add(TKFormDataKeysConfig.UPLOADED_FILE.getKey(), file);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<ProfileDTO> response = callTKExtractAPI(requestEntity);
        log.info("Retrieve result for process_id {} from TK extract service and insert it in the database", process.getId());
        ProcessResult processResult = new ProcessResult(process.getId(), objectMapper.writeValueAsString(response.getBody()));
        processResultRepository.save(processResult);
    }

    /**
     * This method calls TK extract external service and return response as ResponseEntity<ProfileDTO>
     *
     * @param requestEntity the input {@link HttpEntity<MultiValueMap<String, Object>>} represent header and body
     *                      that be send when calling TK extract service.
     * @return ResponseEntity<ProfileDTO>
     */
    protected ResponseEntity<ProfileDTO> callTKExtractAPI(HttpEntity<MultiValueMap<String, Object>> requestEntity) {
        log.info("Entered callTKExtractAPI and calling the TK extract service");
        ResponseEntity<ProfileDTO> response = restTemplate.postForEntity(tkApiConfig.getUrl(), requestEntity,
                ProfileDTO.class);
        return response;
    }

}
