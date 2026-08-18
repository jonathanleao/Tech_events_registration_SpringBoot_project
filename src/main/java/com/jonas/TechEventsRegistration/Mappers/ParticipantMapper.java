package com.jonas.TechEventsRegistration.Mappers;

import com.jonas.TechEventsRegistration.DTO.Participant.ParticipantRequest;
import com.jonas.TechEventsRegistration.DTO.Participant.ParticipantResponse;
import com.jonas.TechEventsRegistration.Entity.Participant;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ParticipantMapper {

    Participant toEntity (ParticipantRequest participantRequest);

    ParticipantResponse toResponse (Participant participant);
}
