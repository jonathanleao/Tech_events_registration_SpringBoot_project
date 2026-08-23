package com.jonas.TechEventsRegistration.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jonas.TechEventsRegistration.DTO.Enrollment.EnrollmentRequest;
import com.jonas.TechEventsRegistration.DTO.Enrollment.EnrollmentResponse;
import com.jonas.TechEventsRegistration.Exceptions.EventAlreadyOccurredException;
import com.jonas.TechEventsRegistration.Exceptions.NoVacanciesAvailableException;
import com.jonas.TechEventsRegistration.Exceptions.NotFoundException;
import com.jonas.TechEventsRegistration.ExceptionsHandler.ExceptionsHandler;
import com.jonas.TechEventsRegistration.Services.EnrollmentServices;
import com.jonas.TechEventsRegistration.util.RequestsCreator.EnrollmentRequestCreator;
import com.jonas.TechEventsRegistration.util.ResponseCreator.EnrollmentResponseCreator;
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

@WebMvcTest(EnrollmentController.class)
@Import(ExceptionsHandler.class)
@AutoConfigureMockMvc(addFilters = false)
class EnrollmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EnrollmentServices enrollmentServices;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    @Test
    @DisplayName("findAll should return the mapped page of enrollment responses")
    void findAllShouldReturnMappedPageOfEnrollmentResponses() throws Exception {
        EnrollmentResponse response = EnrollmentResponseCreator.createEnrollmentResponse();
        Page<EnrollmentResponse> page = new PageImpl<>(List.of(response), PageRequest.of(0, 10),1);

        BDDMockito.when(enrollmentServices.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/Enrollments/admin")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].participant.email").value(response.getParticipant().getEmail()))
                .andExpect(jsonPath("$.content[0].event.eventName").value(response.getEvent().getEventName()));
    }

    @Test
    @DisplayName("findById should return the mapped enrollment response")
    void findByIdShouldReturnMappedEnrollmentResponse() throws Exception {
        EnrollmentResponse response = EnrollmentResponseCreator.createEnrollmentResponse();

        BDDMockito.when(enrollmentServices.findById(response.getId())).thenReturn(response);

        mockMvc.perform(get("/Enrollments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.getId()))
                .andExpect(jsonPath("$.participant.email").value(response.getParticipant().getEmail()))
                .andExpect(jsonPath("$.event.eventName").value(response.getEvent().getEventName()));
    }

    @Test
    @DisplayName("save should create and return the enrollment response")
    void saveShouldCreateAndReturnEnrollmentResponse() throws Exception {
        EnrollmentRequest request = EnrollmentRequestCreator.createEnrollmentRequest();
        EnrollmentResponse response = EnrollmentResponseCreator.createEnrollmentResponse();

        BDDMockito.when(enrollmentServices.save(any(EnrollmentRequest.class))).thenReturn(response);

        mockMvc.perform(post("/Enrollments/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(response.getId()))
                .andExpect(jsonPath("$.event.eventName").value(response.getEvent().getEventName()))
                .andExpect(jsonPath("$.participant.participantName").value(response.getParticipant().getParticipantName()));

        verify(enrollmentServices).save(any(EnrollmentRequest.class));
    }

    @Test
    @DisplayName("update should update and return the enrollment response")
    void updateShouldUpdateAndReturnEnrollmentResponse() throws Exception {
        EnrollmentRequest request = EnrollmentRequestCreator.createEnrollmentRequestUpdated();
        EnrollmentResponse response = EnrollmentResponseCreator.createEnrollmentResponseUpdated();

        BDDMockito.when(enrollmentServices.update(eq(1L), any(EnrollmentRequest.class))).thenReturn(response);

        mockMvc.perform(put("/Enrollments/admin/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.getId().intValue()))
                .andExpect(jsonPath("$.participant.email").value(response.getParticipant().getEmail()));

        verify(enrollmentServices).update(eq(1L), any(EnrollmentRequest.class));
    }

    @Test
    @DisplayName("findById should throw NotFoundException and return the not found message")
    void findByIdShouldThrowNotFoundExceptionAndReturnNotFoundMessage() throws Exception {
        BDDMockito.when(enrollmentServices.findById(99L))
                .thenThrow(new NotFoundException("enrollment not Found whit id: 99"));

        mockMvc.perform(get("/Enrollments/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Not Found Exception"))
                .andExpect(jsonPath("$.message").value("enrollment not Found whit id: 99"));
    }

    @Test
    @DisplayName("save should throw NoVacanciesAvailableException and return the bad request message")
    void saveShouldThrowNoVacanciesAvailableExceptionAndReturnBadRequestMessage() throws Exception {
        EnrollmentRequest request = EnrollmentRequestCreator.createEnrollmentRequest();

        BDDMockito.when(enrollmentServices.save(any(EnrollmentRequest.class)))
                .thenThrow(new NoVacanciesAvailableException("No vacancies dispo for this event"));

        mockMvc.perform(post("/Enrollments/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("No Vacancies Available Exception"))
                .andExpect(jsonPath("$.message").value("No vacancies dispo for this event"));
    }

    @Test
    @DisplayName("save should throw EventAlreadyOccurredException and return the bad request message")
    void saveShouldThrowEventAlreadyOccurredExceptionAndReturnBadRequestMessage() throws Exception {
        EnrollmentRequest request = EnrollmentRequestCreator.createEnrollmentRequest();

        BDDMockito.when(enrollmentServices.save(any(EnrollmentRequest.class)))
                .thenThrow(new EventAlreadyOccurredException("you can´t not subscribe, the event is already occurred "));

        mockMvc.perform(post("/Enrollments/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Event Already Occurred Exception"))
                .andExpect(jsonPath("$.message").value("you can´t not subscribe, the event is already occurred "));
    }

    @Test
    @DisplayName("update should throw EventAlreadyOccurredException and return the bad request message")
    void updateShouldThrowEventAlreadyOccurredExceptionAndReturnBadRequestMessage() throws Exception {
        EnrollmentRequest request = EnrollmentRequestCreator.createEnrollmentRequestUpdated();

        BDDMockito.when(enrollmentServices.update(eq(1L), any(EnrollmentRequest.class)))
                .thenThrow(new EventAlreadyOccurredException("you can´t not subscribe, the event is already occurred "));

        mockMvc.perform(put("/Enrollments/admin/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Event Already Occurred Exception"))
                .andExpect(jsonPath("$.message").value("you can´t not subscribe, the event is already occurred "));
    }

    @Test
    @DisplayName("delete should return no content when enrollment is removed")
    void deleteShouldReturnNoContentWhenEnrollmentIsRemoved() throws Exception {
        doNothing().when(enrollmentServices).delete(1L);

        mockMvc.perform(delete("/Enrollments/admin/1"))
                .andExpect(status().isNoContent());

        verify(enrollmentServices).delete(1L);
    }
}