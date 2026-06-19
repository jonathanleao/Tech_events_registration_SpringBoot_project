package com.jonas.TechEventsRegistration.Services;

import com.jonas.TechEventsRegistration.DTO.EventRequests.EventPostRequest;
import com.jonas.TechEventsRegistration.DTO.EventRequests.EventPutRequest;
import com.jonas.TechEventsRegistration.Entity.Event;
import com.jonas.TechEventsRegistration.Exceptions.NotFoundException;
import com.jonas.TechEventsRegistration.Mappers.EventMapper;
import com.jonas.TechEventsRegistration.Repository.EventRepository;
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
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class EventServicesTest {

    @InjectMocks
    private EventServices eventServices;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private EventMapper eventMapper;

    @BeforeEach
    void setUp() {
        PageImpl<Event> eventPage = new PageImpl<>(List.of(EventCreator.createEventValid()));
        BDDMockito.when(eventRepository.findAll(ArgumentMatchers.any(PageRequest.class))).thenReturn(eventPage);

        BDDMockito.when(eventRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(EventCreator.createEventValid()));

        BDDMockito.when(eventRepository.findByEventNameContaining(ArgumentMatchers.anyString()))
                .thenReturn(List.of(EventCreator.createEventValid()));

        BDDMockito.when(eventRepository.save(ArgumentMatchers.any(Event.class)))
                .thenReturn(EventCreator.createEventValid());

        BDDMockito.willDoNothing().given(eventRepository).delete(ArgumentMatchers.any(Event.class));

        BDDMockito.when(eventMapper.eventToPost(ArgumentMatchers.any(EventPostRequest.class)))
                .thenReturn(EventCreator.createEventValid());

        BDDMockito.willDoNothing().given(eventMapper)
                .eventToPut(ArgumentMatchers.any(EventPutRequest.class), ArgumentMatchers.any(Event.class));
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

        Page<Event> body = eventServices.findAll(PageRequest.of(0,6));

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

        Event byId = eventServices.findById(1L);

        Assertions.assertThat(byId).isNotNull();

        Assertions.assertThat(byId.getEventName()).isEqualTo(expectedEventName);
        Assertions.assertThat(byId.getCategory()).isEqualTo(expectedCategory);
        Assertions.assertThat(byId.getId()).isEqualTo(expectedId);
        Assertions.assertThat(byId.getLocal()).isEqualTo(expectedLocal);
        Assertions.assertThat(byId.getDescription()).isEqualTo(expectedDescription);
        Assertions.assertThat(byId.getEventDateAndHours()).isEqualTo(expectedEventDateAndHours);
        Assertions.assertThat(byId.getVacancies()).isEqualTo(expectedVacancies);
    }

    @Test
    @DisplayName("return NotFoundException when event is not Found")
    void testFindByIdReturnNotFoundExceptionWhenEventIsNotFound() {
        BDDMockito.when(eventRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> eventServices.findById(1L));

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

        List<Event> byName = eventServices.findByName("springBoot");

        Assertions.assertThat(byName).isNotNull();

        Assertions.assertThat(byName.get(0).getEventName()).isEqualTo(expectedEventName);
        Assertions.assertThat(byName.get(0).getCategory()).isEqualTo(expectedCategory);
        Assertions.assertThat(byName.get(0).getId()).isEqualTo(expectedId);
        Assertions.assertThat(byName.get(0).getLocal()).isEqualTo(expectedLocal);
        Assertions.assertThat(byName.get(0).getDescription()).isEqualTo(expectedDescription);
        Assertions.assertThat(byName.get(0).getEventDateAndHours()).isEqualTo(expectedEventDateAndHours);
        Assertions.assertThat(byName.get(0).getVacancies()).isEqualTo(expectedVacancies);

        Assertions.assertThat(byName).isNotEmpty()
                .hasSize(1);
    }

    @Test
    @DisplayName("return empty list of events when event is not found")
    void testReturnEmptyListOfEventsWhenEventIsNotFound() {
        BDDMockito.when(eventRepository.findByEventNameContaining(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());

        List<Event> byName = eventServices.findByName("NotFoundEvent");

        Assertions.assertThat(byName).isEmpty();
    }
    @Test
    @DisplayName("save event when successful")
    void testSaveReturnEventWhenSuccessful() {
        String expectedEventName = EventCreator.createEventValid().getEventName();
        String expectedCategory = EventCreator.createEventValid().getCategory();
        Long expectedId = EventCreator.createEventValid().getId();
        String expectedLocal = EventCreator.createEventValid().getLocal();
        String expectedDescription = EventCreator.createEventValid().getDescription();
        LocalDateTime expectedEventDateAndHours = EventCreator.createEventValid().getEventDateAndHours();
        Integer expectedVacancies = EventCreator.createEventValid().getVacancies();

        Event save = eventServices.save(EventPostAndPutCreator.createEventPostRequest());

        Assertions.assertThat(save).isNotNull();

        Assertions.assertThat(save.getEventName()).isEqualTo(expectedEventName);
        Assertions.assertThat(save.getCategory()).isEqualTo(expectedCategory);
        Assertions.assertThat(save.getId()).isEqualTo(expectedId);
        Assertions.assertThat(save.getLocal()).isEqualTo(expectedLocal);
        Assertions.assertThat(save.getDescription()).isEqualTo(expectedDescription);
        Assertions.assertThat(save.getEventDateAndHours()).isEqualTo(expectedEventDateAndHours);
        Assertions.assertThat(save.getVacancies()).isEqualTo(expectedVacancies);
    }
    @Test
    @DisplayName("update event when successful")
    void testUpdateReturnEventWhenSuccessful() {

        BDDMockito.when(eventRepository.save(ArgumentMatchers.any(Event.class)))
                .thenReturn(EventCreator.createEventUpdated());


        String expectedEventName = EventCreator.createEventUpdated().getEventName();
        String expectedCategory = EventCreator.createEventUpdated().getCategory();
        Long expectedId = EventCreator.createEventUpdated().getId();
        String expectedLocal = EventCreator.createEventUpdated().getLocal();
        String expectedDescription = EventCreator.createEventUpdated().getDescription();
        LocalDateTime expectedEventDateAndHours = EventCreator.createEventUpdated().getEventDateAndHours();
        Integer expectedVacancies = EventCreator.createEventUpdated().getVacancies();

        Event update = eventServices.update(EventPostAndPutCreator.createEventPutRequest());

        Assertions.assertThat(update).isNotNull();

        Assertions.assertThat(update.getEventName()).isEqualTo(expectedEventName);
        Assertions.assertThat(update.getCategory()).isEqualTo(expectedCategory);
        Assertions.assertThat(update.getId()).isEqualTo(expectedId);
        Assertions.assertThat(update.getLocal()).isEqualTo(expectedLocal);
        Assertions.assertThat(update.getDescription()).isEqualTo(expectedDescription);
        Assertions.assertThat(update.getEventDateAndHours()).isEqualTo(expectedEventDateAndHours);
        Assertions.assertThat(update.getVacancies()).isEqualTo(expectedVacancies);
    }

    @Test
    @DisplayName("delete event and return void when successful")
    void testDeleteReturnVoidWhenSuccessful() {
        Assertions.assertThatCode(()-> eventServices.delete(1L))
                .doesNotThrowAnyException();
    }
}
