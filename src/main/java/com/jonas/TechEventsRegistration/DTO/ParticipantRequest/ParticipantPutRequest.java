package com.jonas.TechEventsRegistration.DTO.ParticipantRequest;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class ParticipantPutRequest {
    private Long id;
    private String participantName;
    @Email
    private String email;
    private String phoneNumber;
    private String institution;
}
