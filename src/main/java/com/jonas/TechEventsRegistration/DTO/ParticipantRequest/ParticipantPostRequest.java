package com.jonas.TechEventsRegistration.DTO.ParticipantRequest;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class ParticipantPostRequest {
    private String participantName;
    @Email
    private String email;
    private String phoneNumber;
    private String institution;
}
