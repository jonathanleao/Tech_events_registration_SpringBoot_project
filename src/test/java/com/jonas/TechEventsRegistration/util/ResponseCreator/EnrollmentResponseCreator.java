package com.jonas.TechEventsRegistration.util.ResponseCreator;

import com.jonas.TechEventsRegistration.DTO.Enrollment.EnrollmentResponse;
import com.jonas.TechEventsRegistration.DTO.Participant.ParticipantResponse;
import com.jonas.TechEventsRegistration.DTO.Event.EventResponse;

public class EnrollmentResponseCreator {

    public static EnrollmentResponse createEnrollmentResponse(){
        return EnrollmentResponse.builder()
                .id(1L)
                .participant(ParticipantResponseCreator.createParticipantResponse())
                .event(EventResponseCreator.createEventResponse())
                .build();
    }

    public static EnrollmentResponse createEnrollmentResponseUpdated(){
        return EnrollmentResponse.builder()
                .id(createEnrollmentResponse().getId())
                .participant(ParticipantResponseCreator.createParticipantResponseUpdated())
                .event(EventResponseCreator.createEventResponseUpdated())
                .build();
    }
}
