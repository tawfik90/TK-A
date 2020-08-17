package com.integrationservice.business.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.core.io.ByteArrayResource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.integrationservice.data.entity.Process;
import com.integrationservice.data.entity.ProcessResult;
import com.integrationservice.data.repository.ProcessRepository;
import com.integrationservice.exception.ProcessNotFoundException;
import com.integrationservice.util.FileUtil;

public class IntegrationServiceTest {

    private ProcessRepository processRepository;
    private TKAsyncOperationService tkAsyncOperationService;
    private ObjectMapper objectMapper;
    private FileUtil fileUtil;
    private Process process;
    private Date now = new Date();
    private String json = "{\"firstName\": \"foo\", \"lastName\": \"boo\", \"address\": null}";

    private IntegrationService is;

    @Before
    public void initObj() {
        processRepository = Mockito.mock(ProcessRepository.class);
        tkAsyncOperationService = Mockito.mock(TKAsyncOperationService.class);
        objectMapper = Mockito.mock(ObjectMapper.class);
        fileUtil = Mockito.mock(FileUtil.class);
        process = new Process(12l, now, new ProcessResult(12l, json));
        is = new IntegrationService(processRepository, tkAsyncOperationService, objectMapper, fileUtil);
    }

    @Test
    public void submitCV_Test() throws IOException {
        Mockito.doNothing().when(tkAsyncOperationService).startProcessCV(Mockito.any(ByteArrayResource.class),
                Mockito.any(Process.class));
        Mockito.doReturn(process).when(processRepository).save(Mockito.any(Process.class));
        assertEquals(12l, is.submitCV(null).getProcessId());
    }

    @Test(expected = Test.None.class /* no exception expected */)
    public void getExtractedResult_Test() throws IOException {
        Mockito.doReturn(Optional.of(process)).when(processRepository).findById(12l);
        is.getExtractedResult(12l);
    }

    @Test(expected = ProcessNotFoundException.class /* no exception expected */)
    public void getExtractedResult_throwsException_Test() throws IOException {
        process = null;
        Mockito.doReturn(Optional.ofNullable(process)).when(processRepository).findById(12l);
        is.getExtractedResult(12l);
    }

}