package com.jonas.TechEventsRegistration.Mappers;

import com.jonas.TechEventsRegistration.DTO.EventRequests.EventPostRequest;
import com.jonas.TechEventsRegistration.DTO.EventRequests.EventPutRequest;
import com.jonas.TechEventsRegistration.Entity.Event;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper (componentModel = "spring")
public interface EventMapper {

    Event eventToPost(EventPostRequest eventPostRequest);

    void eventToPut(EventPutRequest eventPutRequest, @MappingTarget Event event);
}
