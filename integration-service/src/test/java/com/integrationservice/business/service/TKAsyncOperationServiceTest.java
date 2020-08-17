package com.integrationservice.business.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.integrationservice.business.model.ProfileDTO;
import com.integrationservice.data.entity.Process;
import com.integrationservice.data.entity.ProcessResult;
import com.integrationservice.data.repository.ProcessResultRepository;
import com.integrationservice.web.config.TKApiConfig;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


public class TKAsyncOperationServiceTest {
    private RestTemplate restTemplate;
    private ProcessResultRepository processResultRepository;
    private TKApiConfig tkApiConfig;
    private ObjectMapper objectMapper;
    private ProcessResult processResult;
    private final Date now = new Date();
    private final String json = "{\"firstName\": \"foo\", \"lastName\": \"boo\", \"address\": null}";
    private TKAsyncOperationService tkService;
    private TKAsyncOperationService tkServiceSpy;
    private ResponseEntity<ProfileDTO> response;
    private ProfileDTO privateDTO;
    private Process process;

    @Before
    public void initObj() {
        restTemplate = Mockito.mock(RestTemplate.class);
        processResultRepository = Mockito.mock(ProcessResultRepository.class);
        tkApiConfig = Mockito.mock(TKApiConfig.class);
        objectMapper = Mockito.mock(ObjectMapper.class);
        tkService = new TKAsyncOperationService(restTemplate, tkApiConfig, processResultRepository, objectMapper);
        processResult = new ProcessResult(12l, "result");
        privateDTO = new ProfileDTO();
        privateDTO.setFirstName("foo");
        privateDTO.setLastName("boo");
        privateDTO.setAddress(new HashMap<String, String>());
        response = new ResponseEntity<ProfileDTO>(privateDTO, HttpStatus.OK);
        process = new Process(12l, now, new ProcessResult(12l, json));
        tkServiceSpy = Mockito.spy(tkService);
    }

    @Test
    public void startProcessCV_Test() throws IOException {
        Mockito.doReturn(response).when(tkServiceSpy).callTKExtractAPI(Mockito.any());
        Mockito.doReturn(processResult).when(processResultRepository).save(Mockito.any(ProcessResult.class));
        tkServiceSpy.startProcessCV(null, process);
        verify(processResultRepository, times(1)).save(Mockito.any(ProcessResult.class));
    }
}