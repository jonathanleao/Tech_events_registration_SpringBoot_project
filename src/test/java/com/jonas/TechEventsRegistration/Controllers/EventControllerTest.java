package com.jonas.TechEventsRegistration.Controllers;

import com.jonas.TechEventsRegistration.DTO.EventRequests.EventPostRequest;
import com.jonas.TechEventsRegistration.DTO.EventRequests.EventPutRequest;
import com.jonas.TechEventsRegistration.Entity.Event;
import com.jonas.TechEventsRegistration.Exceptions.NotFoundException;
import com.jonas.TechEventsRegistration.Services.EventServices;
import com.jonas.TechEventsRegistration.util.EventCreator;
import com.jonas.TechEventsRegistration.util.RequestsCreator.EventPostAndPutCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class EventControllerTest {

    @InjectMocks
    private EventController eventController;

    @Mock
    private EventServices eventServices;

    @BeforeEach
    void setUp() {
        PageImpl<Event> eventPage = new PageImpl<>(List.of(EventCreator.createEventValid()));
        BDDMockito.when(eventServices.findAll(ArgumentMatchers.any())).thenReturn(eventPage);

        BDDMockito.when(eventServices.findById(ArgumentMatchers.anyLong())).thenReturn(EventCreator.createEventValid());

        BDDMockito.when(eventServices.findByName(ArgumentMatchers.anyString()))
                .thenReturn(List.of(EventCreator.createEventValid()));

        BDDMockito.when(eventServices.save(ArgumentMatchers.any(EventPostRequest.class)))
                .thenReturn(EventCreator.createEventValid());

        BDDMockito.when(eventServices.update(ArgumentMatchers.any(EventPutRequest.class)))
                .thenReturn(EventCreator.createEventUpdated());

        BDDMockito.willDoNothing().given(eventServices).delete(ArgumentMatchers.anyLong());
    }

    @Test
    @DisplayName("return  list of events page when successful")
    void testReturnListOfEventsPageableWhenSuccessful() {
        String expectedEventName = EventCreator.createEventValid().getEventName();
        String expectedCategory = EventCreator.createEventValid().getCategory();
        Long expectedId = EventCreator.createEventValid().getId();
        String expectedLocal = EventCreator.createEventValid().getLocal();
        String expectedDescription = EventCreator.createEventValid().getDescription();
        LocalDateTime expectedEventDateAndHours = EventCreator.createEventValid().getEventDateAndHours();
        Integer expectedVacancies = EventCreator.createEventValid().getVacancies();

        Page<Event> body = eventController.findAll(null).getBody();

        Assertions.assertThat(body).isNotNull();

        Assertions.assertThat(body.toList().get(0).getEventName()).isEqualTo(expectedEventName);
        Assertions.assertThat(body.toList().get(0).getCategory()).isEqualTo(expectedCategory);
        Assertions.assertThat(body.toList().get(0).getId()).isEqualTo(expectedId);
        Assertions.assertThat(body.toList().get(0).getLocal()).isEqualTo(expectedLocal);
        Assertions.assertThat(body.toList().get(0).getDescription()).isEqualTo(expectedDescription);
        Assertions.assertThat(body.toList().get(0).getEventDateAndHours()).isEqualTo(expectedEventDateAndHours);
        Assertions.assertThat(body.toList().get(0).getVacancies()).isEqualTo(expectedVacancies);

        Assertions.assertThat(body).isNotEmpty()
                .hasSize(1);
    }

    @Test
    @DisplayName("return event when findById successful")
    void testFindByIdReturnEventWhenSuccessful() {
        String expectedEventName = EventCreator.createEventValid().getEventName();
        String expectedCategory = EventCreator.createEventValid().getCategory();
        Long expectedId = EventCreator.createEventValid().getId();
        String expectedLocal = EventCreator.createEventValid().getLocal();
        String expectedDescription = EventCreator.createEventValid().getDescription();
        LocalDateTime expectedEventDateAndHours = EventCreator.createEventValid().getEventDateAndHours();
        Integer expectedVacancies = EventCreator.createEventValid().getVacancies();

        ResponseEntity<Event> byId = eventController.findById(1L);

        Assertions.assertThat(byId.getBody()).isNotNull();

        Assertions.assertThat(byId.getBody().getEventName()).isEqualTo(expectedEventName);
        Assertions.assertThat(byId.getBody().getCategory()).isEqualTo(expectedCategory);
        Assertions.assertThat(byId.getBody().getId()).isEqualTo(expectedId);
        Assertions.assertThat(byId.getBody().getLocal()).isEqualTo(expectedLocal);
        Assertions.assertThat(byId.getBody().getDescription()).isEqualTo(expectedDescription);
        Assertions.assertThat(byId.getBody().getEventDateAndHours()).isEqualTo(expectedEventDateAndHours);
        Assertions.assertThat(byId.getBody().getVacancies()).isEqualTo(expectedVacancies);
    }

    @Test
    @DisplayName("return NotFoundException when event is not Found")
    void testFindByIdReturnNotFoundExceptionWhenEventIsNotFound() {
        BDDMockito.when(eventServices.findById(ArgumentMatchers.anyLong()))
                .thenThrow(new NotFoundException("id not found"));

        Assertions.assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> eventController.findById(1L));

    }

    @Test
    @DisplayName("return list of events when successful")
    void testReturnListOfEventsWhenSuccessful() {
        String expectedEventName = EventCreator.createEventValid().getEventName();
        String expectedCategory = EventCreator.createEventValid().getCategory();
        Long expectedId = EventCreator.createEventValid().getId();
        String expectedLocal = EventCreator.createEventValid().getLocal();
        String expectedDescription = EventCreator.createEventValid().getDescription();
        LocalDateTime expectedEventDateAndHours = EventCreator.createEventValid().getEventDateAndHours();
        Integer expectedVacancies = EventCreator.createEventValid().getVacancies();

        ResponseEntity<List<Event>> byName = eventController.findByName("springBoot");

        Assertions.assertThat(byName.getBody()).isNotNull();

        Assertions.assertThat(byName.getBody().get(0).getEventName()).isEqualTo(expectedEventName);
        Assertions.assertThat(byName.getBody().get(0).getCategory()).isEqualTo(expectedCategory);
        Assertions.assertThat(byName.getBody().get(0).getId()).isEqualTo(expectedId);
        Assertions.assertThat(byName.getBody().get(0).getLocal()).isEqualTo(expectedLocal);
        Assertions.assertThat(byName.getBody().get(0).getDescription()).isEqualTo(expectedDescription);
        Assertions.assertThat(byName.getBody().get(0).getEventDateAndHours()).isEqualTo(expectedEventDateAndHours);
        Assertions.assertThat(byName.getBody().get(0).getVacancies()).isEqualTo(expectedVacancies);

        Assertions.assertThat(byName.getBody()).isNotEmpty()
                .hasSize(1);
    }

    @Test
    @DisplayName("return empty list of events when event is not found")
    void testReturnEmptyListOfEventsWhenEventIsNotFound() {
        BDDMockito.when(eventServices.findByName(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());

        ResponseEntity<List<Event>> byName = eventController.findByName("NotFoundEvent");

        Assertions.assertThat(byName.getBody()).isEmpty();
    }
    @Test
    @DisplayName("save event when findById successful")
    void testSaveReturnEventWhenSuccessful() {
        String expectedEventName = EventCreator.createEventValid().getEventName();
        String expectedCategory = EventCreator.createEventValid().getCategory();
        Long expectedId = EventCreator.createEventValid().getId();
        String expectedLocal = EventCreator.createEventValid().getLocal();
        String expectedDescription = EventCreator.createEventValid().getDescription();
        LocalDateTime expectedEventDateAndHours = EventCreator.createEventValid().getEventDateAndHours();
        Integer expectedVacancies = EventCreator.createEventValid().getVacancies();

        ResponseEntity<Event> save = eventController.save(EventPostAndPutCreator.createEventPostRequest());

        Assertions.assertThat(save.getBody()).isNotNull();

        Assertions.assertThat(save.getBody().getEventName()).isEqualTo(expectedEventName);
        Assertions.assertThat(save.getBody().getCategory()).isEqualTo(expectedCategory);
        Assertions.assertThat(save.getBody().getId()).isEqualTo(expectedId);
        Assertions.assertThat(save.getBody().getLocal()).isEqualTo(expectedLocal);
        Assertions.assertThat(save.getBody().getDescription()).isEqualTo(expectedDescription);
        Assertions.assertThat(save.getBody().getEventDateAndHours()).isEqualTo(expectedEventDateAndHours);
        Assertions.assertThat(save.getBody().getVacancies()).isEqualTo(expectedVacancies);
    }
    @Test
    @DisplayName("update event when findById successful")
    void testUpdateReturnEventWhenSuccessful() {
        String expectedEventName = EventCreator.createEventUpdated().getEventName();
        String expectedCategory = EventCreator.createEventUpdated().getCategory();
        Long expectedId = EventCreator.createEventUpdated().getId();
        String expectedLocal = EventCreator.createEventUpdated().getLocal();
        String expectedDescription = EventCreator.createEventUpdated().getDescription();
        LocalDateTime expectedEventDateAndHours = EventCreator.createEventUpdated().getEventDateAndHours();
        Integer expectedVacancies = EventCreator.createEventUpdated().getVacancies();

        ResponseEntity<Event> update = eventController.update(EventPostAndPutCreator.createEventPutRequest());

        Assertions.assertThat(update.getBody()).isNotNull();

        Assertions.assertThat(update.getBody().getEventName()).isEqualTo(expectedEventName);
        Assertions.assertThat(update.getBody().getCategory()).isEqualTo(expectedCategory);
        Assertions.assertThat(update.getBody().getId()).isEqualTo(expectedId);
        Assertions.assertThat(update.getBody().getLocal()).isEqualTo(expectedLocal);
        Assertions.assertThat(update.getBody().getDescription()).isEqualTo(expectedDescription);
        Assertions.assertThat(update.getBody().getEventDateAndHours()).isEqualTo(expectedEventDateAndHours);
        Assertions.assertThat(update.getBody().getVacancies()).isEqualTo(expectedVacancies);
    }

    @Test
    @DisplayName("delete event and return void when successful")
    void testDeleteReturnVoidWhenSuccessful() {
        Long id = EventCreator.createEventValid().getId();

        ResponseEntity<Void> delete = eventController.delete(id);

        Assertions.assertThat(delete.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}