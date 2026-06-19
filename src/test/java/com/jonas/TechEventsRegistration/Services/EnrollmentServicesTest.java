package com.jonas.TechEventsRegistration.Services;

import com.jonas.TechEventsRegistration.DTO.EnrollmentRequests.EnrollmentPostRequest;
import com.jonas.TechEventsRegistration.DTO.EnrollmentRequests.EnrollmentPutRequest;
import com.jonas.TechEventsRegistration.Entity.Enrollment;
import com.jonas.TechEventsRegistration.Exceptions.NotFoundException;
import com.jonas.TechEventsRegistration.Mappers.EnrollmentMapper;
import com.jonas.TechEventsRegistration.Repository.EnrollmentRepository;
import com.jonas.TechEventsRegistration.util.EnrollmentCreator;
import com.jonas.TechEventsRegistration.util.RequestsCreator.EnrollmentPostAndPutCreator;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
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

    @BeforeEach
    void setUp() {
        PageImpl<Enrollment> enrollmentPage = new PageImpl<>(List.of(EnrollmentCreator.enrollmentCreatorValid()));
        BDDMockito.when(enrollmentRepository.findAll(ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(enrollmentPage);

        BDDMockito.when(enrollmentRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(EnrollmentCreator.enrollmentCreatorValid()));

        BDDMockito.when(enrollmentRepository.save(ArgumentMatchers.any(Enrollment.class)))
                .thenReturn(EnrollmentCreator.enrollmentCreatorValid());

        BDDMockito.willDoNothing().given(enrollmentRepository).delete(ArgumentMatchers.any(Enrollment.class));

        BDDMockito.when(enrollmentMapper.enrollmentToPost(ArgumentMatchers.any(EnrollmentPostRequest.class)))
                .thenReturn(EnrollmentCreator.enrollmentCreatorValid());

        BDDMockito.willDoNothing().given(enrollmentMapper)
                .enrollmentToPut(ArgumentMatchers.any(EnrollmentPutRequest.class),
                        ArgumentMatchers.any(Enrollment.class));

        BDDMockito.when(eventServices.findById(ArgumentMatchers.anyLong()))
                .thenReturn(EnrollmentCreator.enrollmentCreatorValid().getEvent());

        BDDMockito.when(participantServices.findById(ArgumentMatchers.anyLong()))
                .thenReturn(EnrollmentCreator.enrollmentCreatorValid().getParticipant());


    }

    @Test
    @DisplayName("Return list of Enrollments page when successful")
    void testReturnListOfEnrollmentsPageWhenSuccessful() {
        Long expectedEventId = EnrollmentCreator.enrollmentCreatorValid().getEvent().getId();
        Long expectedParticipantId = EnrollmentCreator.enrollmentCreatorValid().getParticipant().getId();
        LocalDate enrollmentDate = EnrollmentCreator.enrollmentCreatorValid().getEnrollmentDate();

        Page<Enrollment> body = enrollmentServices.findAll(PageRequest.of(0,1));

        Assertions.assertThat(body).isNotNull();
        Assertions.assertThat(body.toList()).isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(body.toList().get(0).getParticipant().getId()).isEqualTo(expectedParticipantId);
        Assertions.assertThat(body.toList().get(0).getEvent().getId()).isEqualTo(expectedEventId);
        Assertions.assertThat(body.toList().get(0).getEnrollmentDate()).isEqualTo(enrollmentDate);

    }

    @Test
    @DisplayName("Return enrollment when successful")
    void testFindByIdReturnEnrollmentWhenSuccessful() {
        Long expectedEventId = EnrollmentCreator.enrollmentCreatorValid().getEvent().getId();
        Long expectedParticipantId = EnrollmentCreator.enrollmentCreatorValid().getParticipant().getId();
        LocalDate enrollmentDate = EnrollmentCreator.enrollmentCreatorValid().getEnrollmentDate();

        Enrollment ById = enrollmentServices.findById(1L);

        Assertions.assertThat(ById).isNotNull();
        Assertions.assertThat(ById.getParticipant().getId()).isEqualTo(expectedParticipantId);
        Assertions.assertThat(ById.getEvent().getId()).isEqualTo(expectedEventId);
        Assertions.assertThat(ById.getEnrollmentDate()).isEqualTo(enrollmentDate);

    }

    @Test
    @DisplayName("Return not found exception when Enrollment is not found")
    void testFindByIdReturnNotFoundExceptionWhenEnrollmentIsNotFound() {
        BDDMockito.when(enrollmentRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> enrollmentServices.findById(1L));
    }

    @Test
    @DisplayName("save enrollment when successful")
    void testSaveReturnEnrollmentWhenSuccessful() {
        Long expectedEventId = EnrollmentCreator.enrollmentCreatorValid().getEvent().getId();
        Long expectedParticipantId = EnrollmentCreator.enrollmentCreatorValid().getParticipant().getId();
        LocalDate enrollmentDate = EnrollmentCreator.enrollmentCreatorValid().getEnrollmentDate();

        Enrollment save = enrollmentServices.save(EnrollmentPostAndPutCreator.createEnrollmentPostRequest());

        Assertions.assertThat(save).isNotNull();
        Assertions.assertThat(save.getParticipant().getId()).isEqualTo(expectedParticipantId);
        Assertions.assertThat(save.getEvent().getId()).isEqualTo(expectedEventId);
        Assertions.assertThat(save.getEnrollmentDate()).isEqualTo(enrollmentDate);

    }

    @Test
    @DisplayName("Update enrollment when successful")
    void testUpdateReturnEnrollmentWhenSuccessful() {

        BDDMockito.when(enrollmentRepository.save(ArgumentMatchers.any(Enrollment.class)))
                .thenReturn(EnrollmentCreator.enrollmentCreatorUpdated());

        Long expectedEventId = EnrollmentCreator.enrollmentCreatorUpdated().getEvent().getId();
        Long expectedParticipantId = EnrollmentCreator.enrollmentCreatorUpdated().getParticipant().getId();
        LocalDate enrollmentDate = EnrollmentCreator.enrollmentCreatorUpdated().getEnrollmentDate();

        Enrollment update = enrollmentServices.update(EnrollmentPostAndPutCreator.createEnrollmentPutRequest());

        Assertions.assertThat(update).isNotNull();
        Assertions.assertThat(update.getParticipant().getId()).isEqualTo(expectedParticipantId);
        Assertions.assertThat(update.getEvent().getId()).isEqualTo(expectedEventId);
        Assertions.assertThat(update.getEnrollmentDate()).isEqualTo(enrollmentDate);

    }

    @Test
    @DisplayName("delete enrollment and return void when successful")
    void testDeleteReturnVoidWhenSuccessful() {
        Assertions.assertThatCode(()-> enrollmentServices.delete(1L))
                .doesNotThrowAnyException();
    }



}