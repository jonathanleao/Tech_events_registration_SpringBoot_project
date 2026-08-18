package com.jonas.TechEventsRegistration.util.RequestsCreator;

import com.jonas.TechEventsRegistration.DTO.Participant.ParticipantRequest;
import com.jonas.TechEventsRegistration.util.ParticipantCreator;

public class ParticipantRequestCreator {

    public static ParticipantRequest createParticipantRequest(){
        return ParticipantRequest.builder()
                .participantName(ParticipantCreator.createParticipant().getParticipantName())
                .email(ParticipantCreator.createParticipant().getEmail())
                .phoneNumber(ParticipantCreator.createParticipant().getPhoneNumber())
                .institution(ParticipantCreator.createParticipant().getInstitution())
                .build();
    }

    public static ParticipantRequest createParticipantRequestUpdated(){
        return ParticipantRequest.builder()
                .participantName(ParticipantCreator.createParticipantUpdated().getParticipantName())
                .email(ParticipantCreator.createParticipantUpdated().getEmail())
                .phoneNumber(ParticipantCreator.createParticipantUpdated().getPhoneNumber())
                .institution(ParticipantCreator.createParticipantUpdated().getInstitution())
                .build();
    }
}
