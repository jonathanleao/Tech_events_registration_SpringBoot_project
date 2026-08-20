package com.jonas.TechEventsRegistration.Services;

import com.jonas.TechEventsRegistration.DTO.Event.EventResponse;
import com.jonas.TechEventsRegistration.DTO.Participant.ParticipantRequest;
import com.jonas.TechEventsRegistration.DTO.Participant.ParticipantResponse;
import com.jonas.TechEventsRegistration.Entity.Participant;
import com.jonas.TechEventsRegistration.Exceptions.NotFoundException;
import com.jonas.TechEventsRegistration.Mappers.ParticipantMapper;
import com.jonas.TechEventsRegistration.Repository.ParticipantRepository;
import com.jonas.TechEventsRegistration.util.ParticipantCreator;
import com.jonas.TechEventsRegistration.util.RequestsCreator.ParticipantRequestCreator;
import com.jonas.TechEventsRegistration.util.ResponseCreator.ParticipantResponseCreator;
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
class ParticipantServicesTest {

    @InjectMocks
    private ParticipantServices participantServices;

    @Mock
    private ParticipantMapper participantMapper;

    @Mock
    private ParticipantRepository participantRepository;

    @Test
    @DisplayName("findAll should return a mapped page of participant responses")
    void findAllShouldReturnPageOfParticipantResponses() {
        Pageable pageable = PageRequest.of(0, 10);
        Participant participant = ParticipantCreator.createParticipantValid();
        ParticipantResponse response = ParticipantResponseCreator.createParticipantResponse();
        Page<Participant> participantsPage = new PageImpl<>(List.of(participant), pageable, 1);

        BDDMockito.when(participantRepository.findAll(pageable)).thenReturn(participantsPage);
        BDDMockito.when(participantMapper.toResponse(participant)).thenReturn(response);

        Page<ParticipantResponse> result = participantServices.findAll(pageable);

        assertThat(result.getContent()).isNotEmpty();
        assertThat(result.getContent().get(0)).isEqualTo(response);
        BDDMockito.verify(participantRepository).findAll(pageable);
        BDDMockito.verify(participantMapper).toResponse(participant);
    }

    @Test
    @DisplayName("findById should return the mapped response when participant exists")
    void findByIdShouldReturnMappedResponseWhenParticipantExists() {
        Participant participant = ParticipantCreator.createParticipantValid();
        ParticipantResponse response = ParticipantResponseCreator.createParticipantResponse();

        BDDMockito.when(participantRepository.findById(participant.getId())).thenReturn(Optional.of(participant));
        BDDMockito.when(participantMapper.toResponse(participant)).thenReturn(response);

        ParticipantResponse result = participantServices.findById(1L);

        assertThat(result).isEqualTo(response);
        BDDMockito.verify(participantRepository).findById(1L);
        BDDMockito.verify(participantMapper).toResponse(participant);
    }

    @Test
    @DisplayName("findById should throw NotFoundException when participant does not exist")
    void findByIdShouldThrowNotFoundExceptionWhenParticipantDoesNotExist() {
        when(participantRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> participantServices.findById(99L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("findByName should return all matching participant responses")
    void findByNameShouldReturnListOfResponses() {
        List<Participant> participants = List.of(ParticipantCreator.createParticipantValid());
        ParticipantResponse participantResponse = ParticipantResponseCreator.createParticipantResponse();

        BDDMockito.when(participantRepository.findByParticipantNameContaining(participantResponse.getParticipantName()))
                .thenReturn(participants);

        BDDMockito.when(participantMapper.toResponse(participants.get(0))).thenReturn(participantResponse);

        List<ParticipantResponse> result = participantServices.findByName(participantResponse.getParticipantName());

        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0)).isEqualTo(participantResponse);
        BDDMockito.verify(participantRepository).findByParticipantNameContaining(participantResponse.getParticipantName());
    }

    @Test
    @DisplayName("save should persist participant and return mapped response")
    void saveShouldPersistParticipantAndReturnResponse() {
        ParticipantRequest request = ParticipantRequestCreator.createParticipantRequest();
        Participant participantToSave = ParticipantCreator.createParticipant();
        Participant savedParticipant = ParticipantCreator.createParticipantValid();
        ParticipantResponse response = ParticipantResponseCreator.createParticipantResponse();

        BDDMockito.when(participantMapper.toEntity(request)).thenReturn(participantToSave);
        BDDMockito.when(participantRepository.save(participantToSave)).thenReturn(savedParticipant);
        BDDMockito.when(participantMapper.toResponse(savedParticipant)).thenReturn(response);

        ParticipantResponse result = participantServices.save(request);

        assertThat(result).isEqualTo(response);
        BDDMockito.verify(participantRepository).save(participantToSave);
    }

    @Test
    @DisplayName("update should save the participant updated and return a response")
    void updateShouldValidateAndPersistParticipantChanges() {
        ParticipantRequest request = ParticipantRequestCreator.createParticipantRequestUpdated();
        Participant existingParticipant = ParticipantCreator.createParticipantValid();
        Participant updatedParticipant = ParticipantCreator.createParticipantUpdated();
        ParticipantResponse updatedResponse = ParticipantResponseCreator.createParticipantResponseUpdated();

        BDDMockito.when(participantRepository.findById(existingParticipant.getId())).thenReturn(Optional.of(existingParticipant));
        BDDMockito.when(participantMapper.toEntity(request)).thenReturn(updatedParticipant);
        BDDMockito.when(participantRepository.save(updatedParticipant)).thenReturn(updatedParticipant);
        BDDMockito.when(participantMapper.toResponse(updatedParticipant)).thenReturn(updatedResponse);

        ParticipantResponse result = participantServices.update(existingParticipant.getId(), request);

        assertThat(result).isEqualTo(updatedResponse);
        BDDMockito.verify(participantRepository).save(updatedParticipant);
    }

    @Test
    @DisplayName("delete should delete participant when it exists")
    void deleteShouldDeleteParticipantWhenItExists() {
        Participant participant = ParticipantCreator.createParticipantValid();

        BDDMockito.when(participantRepository.findById(participant.getId())).thenReturn(Optional.of(participant));

        participantServices.delete(participant.getId());

        verify(participantRepository).deleteById(participant.getId());
    }
}