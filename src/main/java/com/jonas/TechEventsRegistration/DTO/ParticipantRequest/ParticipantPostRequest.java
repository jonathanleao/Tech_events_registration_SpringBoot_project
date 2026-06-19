package com.jonas.TechEventsRegistration.DTO.ParticipantRequest;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParticipantPostRequest {

    @NotBlank
    private String participantName;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String phoneNumber;
    @NotBlank
    private String institution;
}
