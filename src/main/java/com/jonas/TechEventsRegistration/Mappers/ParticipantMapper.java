package com.jonas.TechEventsRegistration.Mappers;

import com.jonas.TechEventsRegistration.DTO.ParticipantRequest.ParticipantPostRequest;
import com.jonas.TechEventsRegistration.DTO.ParticipantRequest.ParticipantPutRequest;
import com.jonas.TechEventsRegistration.Entity.Participant;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ParticipantMapper {

    Participant participantToPost(ParticipantPostRequest participantPostRequest);

    void participantToPut(ParticipantPutRequest participantPutRequest,@MappingTarget Participant participant);
}
