package com.jonas.TechEventsRegistration.Controllers;

import com.jonas.TechEventsRegistration.DTO.EnrollmentRequests.EnrollmentPostRequest;
import com.jonas.TechEventsRegistration.DTO.EnrollmentRequests.EnrollmentPutRequest;
import com.jonas.TechEventsRegistration.Entity.Enrollment;
import com.jonas.TechEventsRegistration.Exceptions.NotFoundException;
import com.jonas.TechEventsRegistration.Services.EnrollmentServices;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class EnrollmentControllerTest {

    @InjectMocks
    private EnrollmentController enrollmentController;

    @Mock
    private EnrollmentServices enrollmentServices;

    @BeforeEach
    void setUp() {
        PageImpl<Enrollment> enrollmentPage = new PageImpl<>(List.of(EnrollmentCreator.enrollmentCreatorValid()));
        BDDMockito.when(enrollmentServices.findAll(ArgumentMatchers.any())).thenReturn(enrollmentPage);

        BDDMockito.when(enrollmentServices.findById(ArgumentMatchers.anyLong()))
                .thenReturn(EnrollmentCreator.enrollmentCreatorValid());

        BDDMockito.when(enrollmentServices.save(ArgumentMatchers.any(EnrollmentPostRequest.class)))
                .thenReturn(EnrollmentCreator.enrollmentCreatorValid());

        BDDMockito.when(enrollmentServices.update(ArgumentMatchers.any(EnrollmentPutRequest.class)))
                .thenReturn(EnrollmentCreator.enrollmentCreatorUpdated());

        BDDMockito.willDoNothing().given(enrollmentServices).delete(ArgumentMatchers.anyLong());
    }

    @Test
    @DisplayName("Return list of Enrollments page when successful")
    void testReturnListOfEnrollmentsPageWhenSuccessful() {
        Long expectedEventId = EnrollmentCreator.enrollmentCreatorValid().getEvent().getId();
        Long expectedParticipantId = EnrollmentCreator.enrollmentCreatorValid().getParticipant().getId();
        LocalDate enrollmentDate = EnrollmentCreator.enrollmentCreatorValid().getEnrollmentDate();

        Page<Enrollment> body = enrollmentController.findAll(null).getBody();

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

        ResponseEntity<Enrollment> ById = enrollmentController.findById(1L);

        Assertions.assertThat(ById.getBody()).isNotNull();
        Assertions.assertThat(ById.getBody().getParticipant().getId()).isEqualTo(expectedParticipantId);
        Assertions.assertThat(ById.getBody().getEvent().getId()).isEqualTo(expectedEventId);
        Assertions.assertThat(ById.getBody().getEnrollmentDate()).isEqualTo(enrollmentDate);

    }

    @Test
    @DisplayName("Return not found exception when Enrollment is not found")
    void testFindByIdReturnNotFoundExceptionWhenEnrollmentIsNotFound() {
        BDDMockito.when(enrollmentServices.findById(ArgumentMatchers.anyLong()))
                .thenThrow(new NotFoundException("Id not found"));

        Assertions.assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> enrollmentController.findById(1L));
    }

    @Test
    @DisplayName("save enrollment when successful")
    void testSaveReturnEnrollmentWhenSuccessful() {
        Long expectedEventId = EnrollmentCreator.enrollmentCreatorValid().getEvent().getId();
        Long expectedParticipantId = EnrollmentCreator.enrollmentCreatorValid().getParticipant().getId();
        LocalDate enrollmentDate = EnrollmentCreator.enrollmentCreatorValid().getEnrollmentDate();

        ResponseEntity<Enrollment> save = enrollmentController
                .save(EnrollmentPostAndPutCreator.createEnrollmentPostRequest());

        Assertions.assertThat(save.getBody()).isNotNull();
        Assertions.assertThat(save.getBody().getParticipant().getId()).isEqualTo(expectedParticipantId);
        Assertions.assertThat(save.getBody().getEvent().getId()).isEqualTo(expectedEventId);
        Assertions.assertThat(save.getBody().getEnrollmentDate()).isEqualTo(enrollmentDate);

    }

    @Test
    @DisplayName("Update enrollment when successful")
    void testUpdateReturnEnrollmentWhenSuccessful() {
        Long expectedEventId = EnrollmentCreator.enrollmentCreatorUpdated().getEvent().getId();
        Long expectedParticipantId = EnrollmentCreator.enrollmentCreatorUpdated().getParticipant().getId();
        LocalDate enrollmentDate = EnrollmentCreator.enrollmentCreatorUpdated().getEnrollmentDate();

        ResponseEntity<Enrollment> update = enrollmentController
                .update(EnrollmentPostAndPutCreator.createEnrollmentPutRequest());

        Assertions.assertThat(update.getBody()).isNotNull();
        Assertions.assertThat(update.getBody().getParticipant().getId()).isEqualTo(expectedParticipantId);
        Assertions.assertThat(update.getBody().getEvent().getId()).isEqualTo(expectedEventId);
        Assertions.assertThat(update.getBody().getEnrollmentDate()).isEqualTo(enrollmentDate);

    }

    @Test
    @DisplayName("delete enrollment and return void when successful")
    void testDeleteReturnVoidWhenSuccessful() {
        Long id = EnrollmentCreator.enrollmentCreatorValid().getId();

        ResponseEntity<Void> delete = enrollmentController.delete(id);

        Assertions.assertThat(delete.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

}