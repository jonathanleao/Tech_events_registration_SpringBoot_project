package com.jonas.TechEventsRegistration.DTO.EnrollmentRequests;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;

@Data
@Getter
public class EnrollmentPutRequest {
    private Long id;
    private Long participantId;
    private Long eventId;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate enrollmentDate;
}
