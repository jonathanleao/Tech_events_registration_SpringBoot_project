package com.jonas.TechEventsRegistration.Services;

import com.jonas.TechEventsRegistration.DTO.ParticipantRequest.ParticipantPostRequest;
import com.jonas.TechEventsRegistration.DTO.ParticipantRequest.ParticipantPutRequest;
import com.jonas.TechEventsRegistration.Entity.Participant;
import com.jonas.TechEventsRegistration.Exceptions.NotFoundException;
import com.jonas.TechEventsRegistration.Mappers.ParticipantMapper;
import com.jonas.TechEventsRegistration.Repository.ParticipantRepository;
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
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ParticipantServicesTest {
 @InjectMocks
 private ParticipantServices participantServices;

 @Mock
 private ParticipantRepository participantRepository;

 @Mock
 private ParticipantMapper participantMapper;

 @BeforeEach
 void setUp() {
  PageImpl<Participant> participantPage = new PageImpl<>(List.of(ParticipantCreator.createParticipantValid()));
  BDDMockito.when(participantRepository.findAll(ArgumentMatchers.any(PageRequest.class)))
          .thenReturn(participantPage);

  BDDMockito.when(participantRepository.findById(ArgumentMatchers.anyLong()))
          .thenReturn(Optional.of(ParticipantCreator.createParticipantValid()));

  BDDMockito.when(participantRepository.findByParticipantNameContaining(ArgumentMatchers.anyString()))
          .thenReturn(List.of(ParticipantCreator.createParticipantValid()));

  BDDMockito.when(participantRepository.save(ArgumentMatchers.any(Participant.class)))
          .thenReturn(ParticipantCreator.createParticipantValid());

  BDDMockito.willDoNothing().given(participantRepository).delete(ArgumentMatchers.any(Participant.class));

  BDDMockito.when(participantMapper.participantToPost(ArgumentMatchers.any(ParticipantPostRequest.class)))
          .thenReturn(ParticipantCreator.createParticipantValid());

  BDDMockito.willDoNothing().given(participantMapper)
          .participantToPut(ArgumentMatchers.any(ParticipantPutRequest.class), ArgumentMatchers.any(Participant.class));
 }

 @Test
 @DisplayName("return list of Participant page when successful")
 void testReturnListOfParticipantsPageableWhenSuccessful() {
  String expectedName = ParticipantCreator.createParticipantValid().getParticipantName();
  String expectedEmail = ParticipantCreator.createParticipantValid().getEmail();
  String expectedInstitution = ParticipantCreator.createParticipantValid().getInstitution();
  String expectedPhoneNumber = ParticipantCreator.createParticipantValid().getPhoneNumber();
  Long expectedId = ParticipantCreator.createParticipantValid().getId();

  Page<Participant> all = participantServices.findAll(PageRequest.of(0, 6));

  Assertions.assertThat(all).isNotNull();

  Assertions.assertThat(all.toList().get(0).getParticipantName()).isEqualTo(expectedName);
  Assertions.assertThat(all.toList().get(0).getInstitution()).isEqualTo(expectedInstitution);
  Assertions.assertThat(all.toList().get(0).getPhoneNumber()).isEqualTo(expectedPhoneNumber);
  Assertions.assertThat(all.toList().get(0).getEmail()).isEqualTo(expectedEmail);
  Assertions.assertThat(all.toList().get(0).getId()).isEqualTo(expectedId);


  Assertions.assertThat(all).isNotEmpty()
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

  Participant byId = participantServices.findById(1L);

  Assertions.assertThat(byId).isNotNull();

  Assertions.assertThat(byId.getParticipantName()).isEqualTo(expectedName);
  Assertions.assertThat(byId.getInstitution()).isEqualTo(expectedInstitution);
  Assertions.assertThat(byId.getPhoneNumber()).isEqualTo(expectedPhoneNumber);
  Assertions.assertThat(byId.getEmail()).isEqualTo(expectedEmail);
  Assertions.assertThat(byId.getId()).isEqualTo(expectedId);

 }

 @Test
 @DisplayName("return NotFoundException when Participant is not found")
 void testFindByIdReturnNotFoundExceptionWhenParticipantIsNotFound() {
  BDDMockito.when(participantRepository.findById(ArgumentMatchers.anyLong()))
          .thenReturn(Optional.empty());

  Assertions.assertThatExceptionOfType(NotFoundException.class)
          .isThrownBy(() -> participantServices.findById(1L));


 }

 @Test
 @DisplayName("return list of Participant when successful")
 void testReturnListOfParticipantsWhenSuccessful() {
  String expectedName = ParticipantCreator.createParticipantValid().getParticipantName();
  String expectedEmail = ParticipantCreator.createParticipantValid().getEmail();
  String expectedInstitution = ParticipantCreator.createParticipantValid().getInstitution();
  String expectedPhoneNumber = ParticipantCreator.createParticipantValid().getPhoneNumber();
  Long expectedId = ParticipantCreator.createParticipantValid().getId();

  List<Participant> byName = participantServices.findByName("jon");

  Assertions.assertThat(byName).isNotNull();

  Assertions.assertThat(byName.get(0).getParticipantName()).isEqualTo(expectedName);
  Assertions.assertThat(byName.get(0).getInstitution()).isEqualTo(expectedInstitution);
  Assertions.assertThat(byName.get(0).getPhoneNumber()).isEqualTo(expectedPhoneNumber);
  Assertions.assertThat(byName.get(0).getEmail()).isEqualTo(expectedEmail);
  Assertions.assertThat(byName.get(0).getId()).isEqualTo(expectedId);


  Assertions.assertThat(byName).isNotEmpty()
          .hasSize(1);
 }

 @Test
 @DisplayName("return empty list of Participant when Participant is not found")
 void testReturnEmptyListOfParticipantsWhenParticipantIsNotFound() {
  BDDMockito.when(participantRepository.findByParticipantNameContaining(ArgumentMatchers.anyString()))
          .thenReturn(Collections.emptyList());

  List<Participant> byName = participantServices.findByName("NotFoundParticipant");

  Assertions.assertThat(byName).isEmpty();

 }

 @Test
 @DisplayName("save Participant when successful")
 void testSaveReturnParticipantWhenSuccessful() {
  String expectedName = ParticipantCreator.createParticipantValid().getParticipantName();
  String expectedEmail = ParticipantCreator.createParticipantValid().getEmail();
  String expectedInstitution = ParticipantCreator.createParticipantValid().getInstitution();
  String expectedPhoneNumber = ParticipantCreator.createParticipantValid().getPhoneNumber();
  Long expectedId = ParticipantCreator.createParticipantValid().getId();

  Participant save = participantServices.save(ParticipantPostAndPutCreator.createParticipantPostRequest());

  Assertions.assertThat(save).isNotNull();
  Assertions.assertThat(save.getParticipantName()).isEqualTo(expectedName);
  Assertions.assertThat(save.getInstitution()).isEqualTo(expectedInstitution);
  Assertions.assertThat(save.getPhoneNumber()).isEqualTo(expectedPhoneNumber);
  Assertions.assertThat(save.getEmail()).isEqualTo(expectedEmail);
  Assertions.assertThat(save.getId()).isEqualTo(expectedId);



 }

 @Test
 @DisplayName("update Participant successful")
 void testUpdateReturnParticipantWhenSuccessful() {
 BDDMockito.when(participantRepository.save(ArgumentMatchers.any(Participant.class)))
           .thenReturn(ParticipantCreator.createParticipantUpdated());

  String expectedName = ParticipantCreator.createParticipantUpdated().getParticipantName();
  String expectedEmail = ParticipantCreator.createParticipantUpdated().getEmail();
  String expectedInstitution = ParticipantCreator.createParticipantUpdated().getInstitution();
  String expectedPhoneNumber = ParticipantCreator.createParticipantUpdated().getPhoneNumber();
  Long expectedId = ParticipantCreator.createParticipantUpdated().getId();

  Participant update = participantServices.update(ParticipantPostAndPutCreator.createParticipantPutRequest());

  Assertions.assertThat(update).isNotNull();
  Assertions.assertThat(update.getParticipantName()).isEqualTo(expectedName);
  Assertions.assertThat(update.getInstitution()).isEqualTo(expectedInstitution);
  Assertions.assertThat(update.getPhoneNumber()).isEqualTo(expectedPhoneNumber);
  Assertions.assertThat(update.getEmail()).isEqualTo(expectedEmail);
  Assertions.assertThat(update.getId()).isEqualTo(expectedId);


 }

 @Test
 @DisplayName("delete participant and return void when successful")
 void testDeleteReturnVoidWhenSuccessful() {
    Assertions.assertThatCode(() -> participantServices.delete(1L))
            .doesNotThrowAnyException();

 }
}