package com.jonas.TechEventsRegistration.util.RequestsCreator;

import com.jonas.TechEventsRegistration.DTO.Event.EventRequest;
import com.jonas.TechEventsRegistration.util.EventCreator;

import java.time.LocalDateTime;

public class EventRequestCreator {

    public static EventRequest createEventRequest(){
        return EventRequest.builder()
                .eventName(EventCreator.createEvent().getEventName())
                .description(EventCreator.createEvent().getDescription())
                .local(EventCreator.createEvent().getLocal())
                .category(EventCreator.createEvent().getCategory())
                .vacancies(50)
                .maxVacancies(50)
                .eventDateAndHours(EventCreator.createEvent().getEventDateAndHours())
                .build();
    }
    public static EventRequest createEventRequestUpdated(){
        return EventRequest.builder()
                .eventName(EventCreator.createEventUpdated().getEventName())
                .description(EventCreator.createEventUpdated().getDescription())
                .local(EventCreator.createEventUpdated().getLocal())
                .category(EventCreator.createEventUpdated().getCategory())
                .vacancies(50)
                .maxVacancies(70)
                .eventDateAndHours(EventCreator.createEventUpdated().getEventDateAndHours())
                .build();
    }
}
