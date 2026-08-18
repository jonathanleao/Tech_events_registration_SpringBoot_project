package com.jonas.TechEventsRegistration.Mappers;

import com.jonas.TechEventsRegistration.DTO.Event.EventRequest;
import com.jonas.TechEventsRegistration.DTO.Event.EventResponse;
import com.jonas.TechEventsRegistration.Entity.Event;
import org.mapstruct.Mapper;

@Mapper (componentModel = "spring")
public interface EventMapper {

    Event toEntity (EventRequest eventRequest);

    EventResponse toResponse (Event Event);


}
