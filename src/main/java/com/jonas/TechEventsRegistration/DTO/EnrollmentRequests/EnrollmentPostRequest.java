package com.jonas.TechEventsRegistration.DTO.EnrollmentRequests;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentPostRequest {

    @NotNull
    private Long participantId;
    @NotNull
    private Long eventId;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate enrollmentDate;

}
