package com.jonas.TechEventsRegistration.DTO.Enrollment;

import com.jonas.TechEventsRegistration.DTO.Event.EventResponse;
import com.jonas.TechEventsRegistration.DTO.Participant.ParticipantResponse;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentResponse {
    private Long Id;
    private ParticipantResponse participant;
    private EventResponse event;
}
