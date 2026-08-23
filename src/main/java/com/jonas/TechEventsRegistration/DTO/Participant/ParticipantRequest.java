package com.jonas.TechEventsRegistration.DTO.Participant;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParticipantRequest {

    @NotBlank
    private String participantName;
    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Pattern(
            regexp = "^\\(\\d{2}\\) 9\\d{4}-\\d{4}$",
            message = "Phone number must follow the pattern (XX) 9XXXX-XXXX"
    )
    @Schema(example = "(92) 98765-4321")
    private String phoneNumber;

    @NotBlank
    private String institution;
}
