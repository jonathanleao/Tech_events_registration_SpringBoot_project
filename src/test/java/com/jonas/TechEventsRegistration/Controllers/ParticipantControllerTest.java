package com.jonas.TechEventsRegistration.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jonas.TechEventsRegistration.DTO.Participant.ParticipantRequest;
import com.jonas.TechEventsRegistration.DTO.Participant.ParticipantResponse;
import com.jonas.TechEventsRegistration.Exceptions.NotFoundException;
import com.jonas.TechEventsRegistration.ExceptionsHandler.ExceptionsHandler;
import com.jonas.TechEventsRegistration.Services.ParticipantServices;
import com.jonas.TechEventsRegistration.util.RequestsCreator.ParticipantRequestCreator;
import com.jonas.TechEventsRegistration.util.ResponseCreator.ParticipantResponseCreator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ParticipantController.class)
@Import(ExceptionsHandler.class)
@AutoConfigureMockMvc(addFilters = false)
class ParticipantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ParticipantServices participantServices;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    @Test
    @DisplayName("findAll should return the mapped page of participant responses")
    void findAllShouldReturnMappedPageOfParticipantResponses() throws Exception {
        ParticipantResponse response = ParticipantResponseCreator.createParticipantResponse();
        Page<ParticipantResponse> page = new PageImpl<>(List.of(response), PageRequest.of(0, 10), 1);

        BDDMockito.when(participantServices.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/Participants/admin")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].participantName").value(response.getParticipantName()))
                .andExpect(jsonPath("$.content[0].email").value(response.getEmail()));
    }

    @Test
    @DisplayName("findById should return the mapped participant response")
    void findByIdShouldReturnMappedParticipantResponse() throws Exception {
        ParticipantResponse response = ParticipantResponseCreator.createParticipantResponse();

        BDDMockito.when(participantServices.findById(1L)).thenReturn(response);

        mockMvc.perform(get("/Participants/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.participantName").value(response.getParticipantName()))
                .andExpect(jsonPath("$.email").value(response.getEmail()));
    }

    @Test
    @DisplayName("findByName should return a list of participant responses")
    void findByNameShouldReturnListOfParticipantResponses() throws Exception {
        ParticipantResponse response = ParticipantResponseCreator.createParticipantResponse();

        BDDMockito.when(participantServices.findByName("Jonathan")).thenReturn(List.of(response));

        mockMvc.perform(get("/Participants/find").param("name", "Jonathan"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].participantName").value(response.getParticipantName()))
                .andExpect(jsonPath("$[0].email").value(response.getEmail()));
    }

    @Test
    @DisplayName("save should create and return the participant response")
    void saveShouldCreateAndReturnParticipantResponse() throws Exception {
        ParticipantRequest request = ParticipantRequestCreator.createParticipantRequest();
        ParticipantResponse response = ParticipantResponseCreator.createParticipantResponse();

        BDDMockito.when(participantServices.save(any(ParticipantRequest.class))).thenReturn(response);

        mockMvc.perform(post("/Participants/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.participantName").value(response.getParticipantName()))
                .andExpect(jsonPath("$.email").value(response.getEmail()));

        verify(participantServices).save(any(ParticipantRequest.class));
    }

    @Test
    @DisplayName("update should update and return the participant response")
    void updateShouldUpdateAndReturnParticipantResponse() throws Exception {
        ParticipantRequest request = ParticipantRequestCreator.createParticipantRequestUpdated();
        ParticipantResponse response = ParticipantResponseCreator.createParticipantResponseUpdated();

        BDDMockito.when(participantServices.update(eq(1L), any(ParticipantRequest.class))).thenReturn(response);

        mockMvc.perform(put("/Participants/admin/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.participantName").value(response.getParticipantName()))
                .andExpect(jsonPath("$.email").value(response.getEmail()));

        verify(participantServices).update(eq(1L), any(ParticipantRequest.class));
    }

    @Test
    @DisplayName("findById should throw NotFoundException and return the not found message")
    void findByIdShouldThrowNotFoundExceptionAndReturnNotFoundMessage() throws Exception {
        BDDMockito.when(participantServices.findById(99L))
                .thenThrow(new NotFoundException("Participant not found with id: 99"));

        mockMvc.perform(get("/Participants/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Not Found Exception"))
                .andExpect(jsonPath("$.message").value("Participant not found with id: 99"));
    }

    @Test
    @DisplayName("delete should return no content when participant is removed")
    void deleteShouldReturnNoContentWhenParticipantIsRemoved() throws Exception {
        doNothing().when(participantServices).delete(1L);

        mockMvc.perform(delete("/Participants/admin/1"))
                .andExpect(status().isNoContent());

        verify(participantServices).delete(1L);
    }
}