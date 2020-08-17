package com.integrationservice.web.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.integrationservice.business.service.IntegrationService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(IntegrationServiceController.class)
class IntegrationServiceControllerTest {

    @MockBean
    IntegrationService integrationService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testSubmit() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test contract.pdf", MediaType.APPLICATION_PDF_VALUE,
                "<<pdf data>>".getBytes(StandardCharsets.UTF_8));

        ObjectMapper objectMapper = new ObjectMapper();

        MockMultipartFile metadata = new MockMultipartFile("request", "request", MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsString("").getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(MockMvcRequestBuilders.multipart("/submit").file(file).file(metadata)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    public void testSubmit_BadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.multipart("/submit").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetExtractedInformation() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/retrieve/12").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetExtractedInformation_BadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/retrieve/null").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}