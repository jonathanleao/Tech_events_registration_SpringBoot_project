package com.jonas.TechEventsRegistration.Services;

import com.jonas.TechEventsRegistration.DTO.Enrollment.EnrollmentRequest;
import com.jonas.TechEventsRegistration.DTO.Enrollment.EnrollmentResponse;
import com.jonas.TechEventsRegistration.Entity.Enrollment;
import com.jonas.TechEventsRegistration.Entity.Event;
import com.jonas.TechEventsRegistration.Entity.Participant;
import com.jonas.TechEventsRegistration.Exceptions.NoVacanciesAvailableException;
import com.jonas.TechEventsRegistration.Exceptions.NotFoundException;
import com.jonas.TechEventsRegistration.Mappers.EnrollmentMapper;
import com.jonas.TechEventsRegistration.Repository.EnrollmentRepository;
import com.jonas.TechEventsRegistration.util.EnrollmentCreator;
import com.jonas.TechEventsRegistration.util.EventCreator;
import com.jonas.TechEventsRegistration.util.ParticipantCreator;
import com.jonas.TechEventsRegistration.util.RequestsCreator.EnrollmentRequestCreator;
import com.jonas.TechEventsRegistration.util.ResponseCreator.EnrollmentResponseCreator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
class EnrollmentServicesTest {

    @InjectMocks
    private EnrollmentServices enrollmentServices;

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private EnrollmentMapper enrollmentMapper;

    @Mock
    private ParticipantServices participantServices;

    @Mock
    private EventServices eventServices;

    @Test
    @DisplayName("findAll should return the mapped page of enrollment responses")
    void findAllShouldReturnMappedPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Enrollment enrollment = EnrollmentCreator.enrollmentCreatorValid();
        EnrollmentResponse response = EnrollmentResponseCreator.createEnrollmentResponse();
        Page<Enrollment> page = new PageImpl<>(List.of(enrollment), pageable, 1);

        BDDMockito.when(enrollmentRepository.findAll(pageable)).thenReturn(page);
        BDDMockito.when(enrollmentMapper.toResponse(enrollment)).thenReturn(response);

        Page<EnrollmentResponse> result = enrollmentServices.findAll(pageable);

        assertThat(result).isNotEmpty();
        assertThat(result.getContent().get(0)).isEqualTo(response);
        BDDMockito.verify(enrollmentRepository).findAll(pageable);
        BDDMockito.verify(enrollmentMapper).toResponse(enrollment);
    }

    @Test
    @DisplayName("findById should return the mapped response when enrollment exists")
    void findByIdShouldReturnMappedResponseWhenEnrollmentExists() {
        Enrollment enrollment = EnrollmentCreator.enrollmentCreatorValid();
        EnrollmentResponse response = EnrollmentResponseCreator.createEnrollmentResponse();

        BDDMockito.when(enrollmentRepository.findById(enrollment.getId())).thenReturn(Optional.of(enrollment));
        BDDMockito.when(enrollmentMapper.toResponse(enrollment)).thenReturn(response);

        EnrollmentResponse result = enrollmentServices.findById(enrollment.getId());

        assertThat(result).isEqualTo(response);
        BDDMockito.verify(enrollmentRepository).findById(1L);
    }

    @Test
    @DisplayName("save should create enrollment, associate event and participant, and reduce vacancies, and return" +
            "a Enrollment response")
    void saveShouldCreateEnrollmentAndDecreaseVacancies() {
        EnrollmentRequest request = EnrollmentRequestCreator.createEnrollmentRequest();
        Event event = EventCreator.createEventValid();
        Participant participant = ParticipantCreator.createParticipantValid();
        Enrollment entity = EnrollmentCreator.enrollmentCreator();
        EnrollmentResponse response = EnrollmentResponseCreator.createEnrollmentResponse();

        BDDMockito.when(eventServices.findEventEntityById(event.getId())).thenReturn(event);
        BDDMockito.when(participantServices.findParticipantEntityById(participant.getId())).thenReturn(participant);
        BDDMockito.when(enrollmentMapper.toEntity(request)).thenReturn(entity);
        BDDMockito.when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(entity);
        BDDMockito.when(enrollmentMapper.toResponse(entity)).thenReturn(response);

        EnrollmentResponse result = enrollmentServices.save(request);

        assertThat(result).isEqualTo(response);
        BDDMockito.verify(enrollmentRepository).save(entity);
    }

    @Test
    @DisplayName("save should throw NotFoundException when participant is missing")
    void saveShouldThrowNotFoundExceptionWhenParticipantDoesNotExist() {
        EnrollmentRequest request = EnrollmentRequestCreator.createEnrollmentRequest();
        Event event = EventCreator.createEventValid();

        BDDMockito.when(eventServices.findEventEntityById(event.getId())).thenReturn(event);
        BDDMockito.when(participantServices.findParticipantEntityById(1L))
                .thenThrow(NotFoundException.class);

        assertThatThrownBy(()-> enrollmentServices.save(request))
                .isInstanceOf(NotFoundException.class);

        BDDMockito.verify(enrollmentRepository, never()).save(any());
    }

    @Test
    @DisplayName("save should reject enrollment when there are no available vacancies")
    void saveShouldThrowNoVacanciesAvailableExceptionWhenVacanciesAreZero() {
        EnrollmentRequest request = EnrollmentRequestCreator.createEnrollmentRequest();
        Event event = EventCreator.createEventValid();
        event.setVacancies(0);
        Participant participant = ParticipantCreator.createParticipantValid();
        Enrollment entity = EnrollmentCreator.enrollmentCreator();

        BDDMockito.when(eventServices.findEventEntityById(event.getId())).thenReturn(event);
        BDDMockito.when(participantServices.findParticipantEntityById(participant.getId())).thenReturn(participant);
        BDDMockito.when(enrollmentMapper.toEntity(request)).thenReturn(entity);

        assertThatThrownBy(()-> enrollmentServices.save(request))
                .isInstanceOf(NoVacanciesAvailableException.class);

        BDDMockito.verify(enrollmentRepository, never()).save(any());
    }

    @Test
    @DisplayName("update should adjust vacancies when the enrollment changes to another event " +
            ", save the event updated and return a response")
    void updateShouldAdjustVacanciesWhenEventChanges() {
        Enrollment enrollment = EnrollmentCreator.enrollmentCreatorValid();
        Event oldEvent = enrollment.getEvent();
        oldEvent.setId(1L);
        oldEvent.setVacancies(2);

        Event newEvent = EventCreator.createEventValid();
        newEvent.setId(2L);
        newEvent.setVacancies(5);
        Participant participant = ParticipantCreator.createParticipantValid();

        EnrollmentRequest request = EnrollmentRequestCreator.createEnrollmentRequest();
        request.setEventId(2L);

        when(enrollmentRepository.findById(enrollment.getId())).thenReturn(Optional.of(enrollment));
        when(eventServices.findEventEntityById(newEvent.getId())).thenReturn(newEvent);
        when(participantServices.findParticipantEntityById(participant.getId())).thenReturn(participant);

        enrollmentServices.update(1L, request);

        assertThat(oldEvent.getVacancies()).isEqualTo(3);
        assertThat(newEvent.getVacancies()).isEqualTo(4);
        BDDMockito.verify(enrollmentRepository).save(enrollment);
    }

    @Test
    @DisplayName("delete should increase the event vacancies when enrollment is removed")
    void deleteShouldIncreaseVacanciesWhenEnrollmentIsDeleted() {
        Enrollment enrollment = EnrollmentCreator.enrollmentCreatorValid();
        enrollment.getEvent().setVacancies(10);

        BDDMockito.when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment));

        enrollmentServices.delete(1L);

        assertThat(enrollment.getEvent().getVacancies()).isEqualTo(11);
        BDDMockito.verify(enrollmentRepository).deleteById(1L);
    }

}
