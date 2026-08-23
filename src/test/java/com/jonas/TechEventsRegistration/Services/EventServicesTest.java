package com.jonas.TechEventsRegistration.Services;

import com.jonas.TechEventsRegistration.DTO.Event.EventRequest;
import com.jonas.TechEventsRegistration.DTO.Event.EventResponse;
import com.jonas.TechEventsRegistration.Entity.Event;
import com.jonas.TechEventsRegistration.Exceptions.NotFoundException;
import com.jonas.TechEventsRegistration.Exceptions.VacanciesLimitExceedException;
import com.jonas.TechEventsRegistration.Mappers.EventMapper;
import com.jonas.TechEventsRegistration.Repository.EventRepository;
import com.jonas.TechEventsRegistration.util.EventCreator;
import com.jonas.TechEventsRegistration.util.RequestsCreator.EventRequestCreator;
import com.jonas.TechEventsRegistration.util.ResponseCreator.EventResponseCreator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServicesTest {

    @InjectMocks
    private EventServices eventServices;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private EventMapper eventMapper;

    @Test
    @DisplayName("findAll should return the mapped page of event responses")
    void findAllShouldReturnMappedPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Event event = EventCreator.createEventValid();
        EventResponse response = EventResponseCreator.createEventResponse();
        Page<Event> page = new PageImpl<>(List.of(event), pageable, 1);

        BDDMockito.when(eventRepository.findAll(pageable)).thenReturn(page);
        when(eventMapper.toResponse(event)).thenReturn(response);

        Page<EventResponse> result = eventServices.findAll(pageable);

        assertThat(result).isNotEmpty();
        assertThat(result.getContent().get(0)).isEqualTo(response);
    }

    @Test
    @DisplayName("findById should return mapped response when the event exists")
    void findByIdShouldReturnMappedResponseWhenEventExists() {
        Event event = EventCreator.createEventValid();
        EventResponse response = EventResponseCreator.createEventResponse();

        BDDMockito.when(eventRepository.findById(event.getId())).thenReturn(Optional.of(event));
        BDDMockito.when(eventMapper.toResponse(event)).thenReturn(response);

        EventResponse result = eventServices.findById(event.getId());

        assertThat(result).isEqualTo(response);
        BDDMockito.verify(eventRepository).findById(event.getId());
    }

    @Test
    @DisplayName("findById should throw NotFoundException when the event does not exist")
    void findByIdShouldThrowNotFoundExceptionWhenEventDoesNotExist() {
        BDDMockito.when(eventRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(()-> eventServices.findById(99L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("findByName should return a List event responses")
    void findByNameShouldReturnListOfResponses() {
        Event event = EventCreator.createEventValid();
        EventResponse response = EventResponseCreator.createEventResponse();

        BDDMockito.when(eventRepository.findByEventNameContaining(event.getEventName())).thenReturn(List.of(event));
        BDDMockito.when(eventMapper.toResponse(event)).thenReturn(response);

        List<EventResponse> result = eventServices.findByName(event.getEventName());

        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0)).isEqualTo(response);
        BDDMockito.verify(eventRepository).findByEventNameContaining(event.getEventName());
    }

    @Test
    @DisplayName("save should persist the event when the vacancy count is within the limit")
    void saveShouldPersistEventWhenVacanciesAreWithinLimit() {
        EventRequest request = EventRequestCreator.createEventRequest();
        Event entity = EventCreator.createEvent();
        Event savedEntity = EventCreator.createEventValid();
        EventResponse response = EventResponseCreator.createEventResponse();

        BDDMockito.when(eventMapper.toEntity(request)).thenReturn(entity);
        BDDMockito.when(eventRepository.save(entity)).thenReturn(savedEntity);
        BDDMockito.when(eventMapper.toResponse(savedEntity)).thenReturn(response);

        EventResponse result = eventServices.save(request);

        assertThat(result).isEqualTo(response);
        BDDMockito.verify(eventRepository).save(entity);
    }

    @Test
    @DisplayName("save should reject a request when vacancies exceed maxVacancies")
    void saveShouldThrowVacanciesLimitExceedExceptionWhenVacanciesExceedLimit() {
        EventRequest request = EventRequestCreator.createEventRequest();
        request.setVacancies(51);
        request.setMaxVacancies(50);

        assertThatThrownBy(() -> eventServices.save(request))
                .isInstanceOf(VacanciesLimitExceedException.class);
        BDDMockito.verify(eventRepository, never()).save(any());
    }

    @Test
    @DisplayName("update should save event updated and return a response")
    void updateShouldSaveEventUpdatedAndReturnAResponse() {
        EventRequest request = EventRequestCreator.createEventRequestUpdated();
        Event existingEvent = EventCreator.createEventValid();
        Event updatedEntity = EventCreator.createEventUpdated();
        Event updateEventValid = EventCreator.createEventValid();
        EventResponse updatedResponse = EventResponseCreator.createEventResponseUpdated();

        BDDMockito.when(eventRepository.findById(existingEvent.getId())).thenReturn(Optional.of(existingEvent));
        BDDMockito.when(eventMapper.toEntity(request)).thenReturn(updatedEntity);
        BDDMockito.when(eventRepository.save(updatedEntity)).thenReturn(updateEventValid);
        BDDMockito.when(eventMapper.toResponse(updatedEntity)).thenReturn(updatedResponse);

        EventResponse result = eventServices.update(existingEvent.getId(), request);

        assertThat(result).isEqualTo(updatedResponse);
        BDDMockito.verify(eventRepository).save(updatedEntity);
    }

    @Test
    @DisplayName("update should throw VacanciesLimitExceedExceptions values where vacancies exceed maxVacancies")
    void updateShouldThrowVacanciesLimitExceedExceptionWhenVacanciesExceedLimit() {
        EventRequest request = EventRequestCreator.createEventRequest();
        Event eventValid = EventCreator.createEventValid();
        request.setVacancies(51);
        request.setMaxVacancies(50);

        BDDMockito.when(eventRepository.findById(eventValid.getId())).thenReturn(Optional.of(eventValid));

        assertThatThrownBy(()-> eventServices.update(eventValid.getId(), request))
                .isInstanceOf(VacanciesLimitExceedException.class);

        BDDMockito.verify(eventRepository, never()).save(any());
    }

    @Test
    @DisplayName("delete should remove the event when it exists")
    void deleteShouldDeleteExistingEvent() {
        Event event = EventCreator.createEventValid();

        BDDMockito.when(eventRepository.findById(event.getId())).thenReturn(Optional.of(event));


        eventServices.delete(event.getId());

        verify(eventRepository).deleteById(event.getId());
    }
}
