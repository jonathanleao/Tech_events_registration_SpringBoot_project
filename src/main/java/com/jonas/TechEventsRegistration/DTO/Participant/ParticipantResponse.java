package com.jonas.TechEventsRegistration.DTO.Participant;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParticipantResponse {
    private String participantName;
    private String email;
}
