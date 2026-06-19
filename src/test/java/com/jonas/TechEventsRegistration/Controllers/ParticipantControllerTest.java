package com.jonas.TechEventsRegistration.Controllers;

import com.jonas.TechEventsRegistration.DTO.ParticipantRequest.ParticipantPostRequest;
import com.jonas.TechEventsRegistration.DTO.ParticipantRequest.ParticipantPutRequest;
import com.jonas.TechEventsRegistration.Entity.Participant;
import com.jonas.TechEventsRegistration.Exceptions.NotFoundException;
import com.jonas.TechEventsRegistration.Services.ParticipantServices;
import com.jonas.TechEventsRegistration.util.ParticipantCreator;
import com.jonas.TechEventsRegistration.util.RequestsCreator.ParticipantPostAndPutCreator;
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

import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ParticipantControllerTest {

    @InjectMocks
    private ParticipantController participantController;

    @Mock
    private ParticipantServices participantServices;

    @BeforeEach
    void setUp() {
        PageImpl<Participant> participantPage = new PageImpl<>(List.of(ParticipantCreator.createParticipantValid()));
        BDDMockito.when(participantServices.findAll(ArgumentMatchers.any())).thenReturn(participantPage);

        BDDMockito.when(participantServices.findById(ArgumentMatchers.anyLong()))
                .thenReturn(ParticipantCreator.createParticipantValid());

        BDDMockito.when(participantServices.findByName(ArgumentMatchers.anyString()))
                .thenReturn(List.of(ParticipantCreator.createParticipantValid()));

        BDDMockito.when(participantServices.save(ArgumentMatchers.any(ParticipantPostRequest.class)))
                .thenReturn(ParticipantCreator.createParticipantValid());

        BDDMockito.when(participantServices.update(ArgumentMatchers.any(ParticipantPutRequest.class)))
                .thenReturn(ParticipantCreator.createParticipantUpdated());

        BDDMockito.willDoNothing().given(participantServices).delete(ArgumentMatchers.anyLong());

    }

    @Test
    @DisplayName("return list of Participant page when successful")
    void testReturnListOfParticipantsPageableWhenSuccessful() {
        String expectedName = ParticipantCreator.createParticipantValid().getParticipantName();
        String expectedEmail = ParticipantCreator.createParticipantValid().getEmail();
        String expectedInstitution = ParticipantCreator.createParticipantValid().getInstitution();
        String expectedPhoneNumber = ParticipantCreator.createParticipantValid().getPhoneNumber();
        Long expectedId = ParticipantCreator.createParticipantValid().getId();

        Page<Participant> body = participantController.findAll(null).getBody();

        Assertions.assertThat(body).isNotNull();

        Assertions.assertThat(body.toList().get(0).getParticipantName()).isEqualTo(expectedName);
        Assertions.assertThat(body.toList().get(0).getInstitution()).isEqualTo(expectedInstitution);
        Assertions.assertThat(body.toList().get(0).getPhoneNumber()).isEqualTo(expectedPhoneNumber);
        Assertions.assertThat(body.toList().get(0).getEmail()).isEqualTo(expectedEmail);
        Assertions.assertThat(body.toList().get(0).getId()).isEqualTo(expectedId);


        Assertions.assertThat(body).isNotEmpty()
                .hasSize(1);
    }

    @Test
    @DisplayName("return Participant when findById successful")
    void testFindByIdReturnParticipantWhenSuccessful() {
        String expectedName = ParticipantCreator.createParticipantValid().getParticipantName();
        String expectedEmail = ParticipantCreator.createParticipantValid().getEmail();
        String expectedInstitution = ParticipantCreator.createParticipantValid().getInstitution();
        String expectedPhoneNumber = ParticipantCreator.createParticipantValid().getPhoneNumber();
        Long expectedId = ParticipantCreator.createParticipantValid().getId();

        ResponseEntity<Participant> byId = participantController.findById(1L);

        Assertions.assertThat(byId.getBody()).isNotNull();

        Assertions.assertThat(byId.getBody().getParticipantName()).isEqualTo(expectedName);
        Assertions.assertThat(byId.getBody().getInstitution()).isEqualTo(expectedInstitution);
        Assertions.assertThat(byId.getBody().getPhoneNumber()).isEqualTo(expectedPhoneNumber);
        Assertions.assertThat(byId.getBody().getEmail()).isEqualTo(expectedEmail);
        Assertions.assertThat(byId.getBody().getId()).isEqualTo(expectedId);

    }

    @Test
    @DisplayName("return NotFoundException when Participant is not found")
    void testFindByIdReturnNotFoundExceptionWhenParticipantIsNotFound() {
        BDDMockito.when(participantServices.findById(ArgumentMatchers.anyLong()))
                .thenThrow(new NotFoundException("Id not found"));

        Assertions.assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> participantController.findById(1L));


    }

    @Test
    @DisplayName("return list of Participant when successful")
    void testReturnListOfParticipantsWhenSuccessful() {
        String expectedName = ParticipantCreator.createParticipantValid().getParticipantName();
        String expectedEmail = ParticipantCreator.createParticipantValid().getEmail();
        String expectedInstitution = ParticipantCreator.createParticipantValid().getInstitution();
        String expectedPhoneNumber = ParticipantCreator.createParticipantValid().getPhoneNumber();
        Long expectedId = ParticipantCreator.createParticipantValid().getId();

        ResponseEntity<List<Participant>> byName = participantController.findByName("jon");

        Assertions.assertThat(byName.getBody()).isNotNull();

        Assertions.assertThat(byName.getBody().get(0).getParticipantName()).isEqualTo(expectedName);
        Assertions.assertThat(byName.getBody().get(0).getInstitution()).isEqualTo(expectedInstitution);
        Assertions.assertThat(byName.getBody().get(0).getPhoneNumber()).isEqualTo(expectedPhoneNumber);
        Assertions.assertThat(byName.getBody().get(0).getEmail()).isEqualTo(expectedEmail);
        Assertions.assertThat(byName.getBody().get(0).getId()).isEqualTo(expectedId);


        Assertions.assertThat(byName.getBody()).isNotEmpty()
                .hasSize(1);
    }

    @Test
    @DisplayName("return empty list of Participant when Participant is not found")
    void testReturnEmptyListOfParticipantsWhenParticipantIsNotFound() {
        BDDMockito.when(participantServices.findByName(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());

        ResponseEntity<List<Participant>> byName = participantController.findByName("NotFoundParticipant");

        Assertions.assertThat(byName.getBody()).isEmpty();

    }

    @Test
    @DisplayName("save Participant when findById successful")
    void testSaveReturnParticipantWhenSuccessful() {
        String expectedName = ParticipantCreator.createParticipantValid().getParticipantName();
        String expectedEmail = ParticipantCreator.createParticipantValid().getEmail();
        String expectedInstitution = ParticipantCreator.createParticipantValid().getInstitution();
        String expectedPhoneNumber = ParticipantCreator.createParticipantValid().getPhoneNumber();
        Long expectedId = ParticipantCreator.createParticipantValid().getId();

        ResponseEntity<Participant> save = participantController
                .save(ParticipantPostAndPutCreator.createParticipantPostRequest());

        Assertions.assertThat(save.getBody()).isNotNull();
        Assertions.assertThat(save.getBody().getParticipantName()).isEqualTo(expectedName);
        Assertions.assertThat(save.getBody().getInstitution()).isEqualTo(expectedInstitution);
        Assertions.assertThat(save.getBody().getPhoneNumber()).isEqualTo(expectedPhoneNumber);
        Assertions.assertThat(save.getBody().getEmail()).isEqualTo(expectedEmail);
        Assertions.assertThat(save.getBody().getId()).isEqualTo(expectedId);

        Assertions.assertThat(save.getStatusCode()).isEqualTo(HttpStatus.CREATED);

    }

    @Test
    @DisplayName("update Participant when findById successful")
    void testUpdateReturnParticipantWhenSuccessful() {
        String expectedName = ParticipantCreator.createParticipantUpdated().getParticipantName();
        String expectedEmail = ParticipantCreator.createParticipantUpdated().getEmail();
        String expectedInstitution = ParticipantCreator.createParticipantUpdated().getInstitution();
        String expectedPhoneNumber = ParticipantCreator.createParticipantUpdated().getPhoneNumber();
        Long expectedId = ParticipantCreator.createParticipantUpdated().getId();

        ResponseEntity<Participant> update = participantController
                .update(ParticipantPostAndPutCreator.createParticipantPutRequest());

        Assertions.assertThat(update.getBody()).isNotNull();
        Assertions.assertThat(update.getBody().getParticipantName()).isEqualTo(expectedName);
        Assertions.assertThat(update.getBody().getInstitution()).isEqualTo(expectedInstitution);
        Assertions.assertThat(update.getBody().getPhoneNumber()).isEqualTo(expectedPhoneNumber);
        Assertions.assertThat(update.getBody().getEmail()).isEqualTo(expectedEmail);
        Assertions.assertThat(update.getBody().getId()).isEqualTo(expectedId);

        Assertions.assertThat(update.getStatusCode()).isEqualTo(HttpStatus.OK);

    }

    @Test
    @DisplayName("delete participant and return void when successful")
    void testDeleteReturnVoidWhenSuccessful() {
        Long id = ParticipantCreator.createParticipantValid().getId();

        ResponseEntity<Void> delete = participantController.delete(id);

        Assertions.assertThat(delete.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }


}