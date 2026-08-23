package com.jonas.TechEventsRegistration.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jonas.TechEventsRegistration.DTO.Event.EventRequest;
import com.jonas.TechEventsRegistration.DTO.Event.EventResponse;
import com.jonas.TechEventsRegistration.Exceptions.NotFoundException;
import com.jonas.TechEventsRegistration.Exceptions.VacanciesLimitExceedException;
import com.jonas.TechEventsRegistration.ExceptionsHandler.ExceptionsHandler;
import com.jonas.TechEventsRegistration.Services.EventServices;
import com.jonas.TechEventsRegistration.util.RequestsCreator.EventRequestCreator;
import com.jonas.TechEventsRegistration.util.ResponseCreator.EventResponseCreator;
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

@WebMvcTest(EventController.class)
@Import(ExceptionsHandler.class)
@AutoConfigureMockMvc(addFilters = false)
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EventServices eventServices;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    @Test
    @DisplayName("findAll should return the mapped page of event responses")
    void findAllShouldReturnMappedPageOfEventResponses() throws Exception {
        EventResponse response = EventResponseCreator.createEventResponse();
        Page<EventResponse> page = new PageImpl<>(List.of(response), PageRequest.of(0, 10), 1);

        BDDMockito.when(eventServices.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/Events/admin")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content",hasSize(1)))
                .andExpect(jsonPath("$.content[0].eventName").value(response.getEventName()))
                .andExpect(jsonPath("$.content[0].category").value(response.getCategory()));
    }

    @Test
    @DisplayName("findById should return the mapped event response")
    void findByIdShouldReturnMappedEventResponse() throws Exception {
        EventResponse response = EventResponseCreator.createEventResponse();

        BDDMockito.when(eventServices.findById(1L)).thenReturn(response);

        mockMvc.perform(get("/Events/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventName").value(response.getEventName()))
                .andExpect(jsonPath("$.category").value(response.getCategory()));
    }

    @Test
    @DisplayName("findByName should return a list of event responses")
    void findByNameShouldReturnListOfEventResponses() throws Exception {
        EventResponse response = EventResponseCreator.createEventResponse();

        BDDMockito.when(eventServices.findByName("Tech Day")).thenReturn(List.of(response));

        mockMvc.perform(get("/Events/find").param("name", "Tech Day"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].eventName").value(response.getEventName()))
                .andExpect(jsonPath("$[0].local").value(response.getLocal()));
    }

    @Test
    @DisplayName("save should create and return the event response")
    void saveShouldCreateAndReturnEventResponse() throws Exception {
        EventRequest request = EventRequestCreator.createEventRequest();
        EventResponse response = EventResponseCreator.createEventResponse();

        BDDMockito.when(eventServices.save(any(EventRequest.class))).thenReturn(response);

        mockMvc.perform(post("/Events/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.eventName").value(response.getEventName()))
                .andExpect(jsonPath("$.description").value(response.getDescription()));

        verify(eventServices).save(any(EventRequest.class));
    }

    @Test
    @DisplayName("update should update and return the event response")
    void updateShouldUpdateAndReturnEventResponse() throws Exception {
        EventRequest request = EventRequestCreator.createEventRequestUpdated();
        EventResponse response = EventResponseCreator.createEventResponseUpdated();

        BDDMockito.when(eventServices.update(eq(1L), any(EventRequest.class))).thenReturn(response);

        mockMvc.perform(put("/Events/admin/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventName").value(response.getEventName()))
                .andExpect(jsonPath("$.local").value(response.getLocal()));

        verify(eventServices).update(eq(1L), any(EventRequest.class));
    }

    @Test
    @DisplayName("findById should throw NotFoundException and return the not found message")
    void findByIdShouldThrowNotFoundExceptionAndReturnNotFoundMessage() throws Exception {
        BDDMockito.when(eventServices.findById(99L))
                .thenThrow(new NotFoundException("Event not found with id: 99"));

        mockMvc.perform(get("/Events/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Not Found Exception"))
                .andExpect(jsonPath("$.message").value("Event not found with id: 99"));
    }

    @Test
    @DisplayName("save should throw VacanciesLimitExceedException and return the bad request message")
    void saveShouldThrowVacanciesLimitExceedExceptionAndReturnBadRequestMessage() throws Exception {
        EventRequest request = EventRequestCreator.createEventRequest();

        BDDMockito.when(eventServices.save(any(EventRequest.class)))
                .thenThrow(new VacanciesLimitExceedException("vacancies cant not exceed the limit"));

        mockMvc.perform(post("/Events/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Vacancies Limit Exceed Exception"))
                .andExpect(jsonPath("$.message").value("vacancies cant not exceed the limit"));
    }

    @Test
    @DisplayName("delete should return no content when event is removed")
    void deleteShouldReturnNoContentWhenEventIsRemoved() throws Exception {
        doNothing().when(eventServices).delete(1L);

        mockMvc.perform(delete("/Events/admin/1"))
                .andExpect(status().isNoContent());

        verify(eventServices).delete(1L);
    }
}