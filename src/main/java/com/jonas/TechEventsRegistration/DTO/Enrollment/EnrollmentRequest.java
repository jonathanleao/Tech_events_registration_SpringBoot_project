package com.jonas.TechEventsRegistration.DTO.Enrollment;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentRequest {

    @NotNull
    private Long participantId;
    @NotNull
    private Long eventId;

    @JsonFormat(pattern = "dd/MM" + "/yyyy")
    private LocalDate enrollmentDate;
}
