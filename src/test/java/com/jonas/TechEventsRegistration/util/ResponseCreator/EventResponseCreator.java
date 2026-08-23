package com.jonas.TechEventsRegistration.util.ResponseCreator;

import com.jonas.TechEventsRegistration.DTO.Event.EventResponse;
import com.jonas.TechEventsRegistration.util.EventCreator;

import java.time.LocalDateTime;

public class EventResponseCreator {

    public static EventResponse createEventResponse(){
        return EventResponse.builder()
                .eventName(EventCreator.createEventValid().getEventName())
                .description(EventCreator.createEventValid().getDescription())
                .local(EventCreator.createEventValid().getLocal())
                .category(EventCreator.createEventValid().getCategory())
                .eventDateAndHours(EventCreator.createEventValid().getEventDateAndHours())
                .build();
    }

    public static EventResponse createEventResponseUpdated(){
        return EventResponse.builder()
                .eventName(EventCreator.createEventUpdated().getEventName())
                .description(EventCreator.createEventValid().getDescription())
                .local(EventCreator.createEventValid().getLocal())
                .category(EventCreator.createEventValid().getCategory())
                .eventDateAndHours(EventCreator.createEventValid().getEventDateAndHours())
                .build();
    }
}
