package com.jonas.TechEventsRegistration.util.RequestsCreator;

import com.jonas.TechEventsRegistration.DTO.ParticipantRequest.ParticipantPostRequest;
import com.jonas.TechEventsRegistration.DTO.ParticipantRequest.ParticipantPutRequest;
import com.jonas.TechEventsRegistration.util.ParticipantCreator;

public class ParticipantPostAndPutCreator {
    public static ParticipantPostRequest createParticipantPostRequest(){
        return ParticipantPostRequest.builder()
                .participantName(ParticipantCreator.createParticipant().getParticipantName())
                .email(ParticipantCreator.createParticipant().getEmail())
                .phoneNumber(ParticipantCreator.createParticipant().getPhoneNumber())
                .institution(ParticipantCreator.createParticipant().getInstitution())
                .build();
    }
    public static ParticipantPutRequest createParticipantPutRequest(){
        return ParticipantPutRequest.builder()
                .id(ParticipantCreator.createParticipantUpdated().getId())
                .participantName(ParticipantCreator.createParticipantUpdated().getParticipantName())
                .email(ParticipantCreator.createParticipantUpdated().getEmail())
                .phoneNumber(ParticipantCreator.createParticipantUpdated().getPhoneNumber())
                .institution(ParticipantCreator.createParticipantUpdated().getInstitution())
                .build();
    }
}
