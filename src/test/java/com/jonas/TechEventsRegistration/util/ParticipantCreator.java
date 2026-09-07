package com.jonas.TechEventsRegistration.util;

import com.jonas.TechEventsRegistration.Entity.Participant;

public class ParticipantCreator {

    public static Participant createParticipant() {
        return Participant.builder()
                .participantName("Jonathan")
                .institution("Uninorte Djalma Batista")
                .email("Jonathan.Leao@Hotmail.com")
                .phoneNumber("(92) 99999-9999").build();
    }

    public static Participant createParticipantValid() {
        return Participant.builder()
                .id(1L)
                .participantName(createParticipant().getParticipantName())
                .institution(createParticipant().getInstitution())
                .email(createParticipant().getEmail())
                .phoneNumber(createParticipant().getPhoneNumber()).build();
    }

    public static Participant createParticipantUpdated() {
        return Participant.builder()
                .id(createParticipantValid().getId())
                .participantName("Jonathan update")
                .institution("UFAM-Universidade Federal Do Amazonas")
                .email("Jonathan.Leao.update@Hotmail.com")
                .phoneNumber("(92) 99999-9999").build();
    }
}
