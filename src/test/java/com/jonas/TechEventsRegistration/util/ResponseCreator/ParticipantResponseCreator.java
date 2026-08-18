package com.jonas.TechEventsRegistration.util.ResponseCreator;

import com.jonas.TechEventsRegistration.DTO.Participant.ParticipantResponse;
import com.jonas.TechEventsRegistration.util.ParticipantCreator;

public class ParticipantResponseCreator {

    public static ParticipantResponse createParticipantResponse(){
        return ParticipantResponse.builder()
                .participantName(ParticipantCreator.createParticipantValid().getParticipantName())
                .email(ParticipantCreator.createParticipantValid().getEmail())
                .build();
    }

    public static ParticipantResponse createParticipantResponseUpdated(){
        return ParticipantResponse.builder()
                .participantName(ParticipantCreator.createParticipantUpdated().getParticipantName())
                .email(ParticipantCreator.createParticipantUpdated().getEmail())
                .build();
    }
}
