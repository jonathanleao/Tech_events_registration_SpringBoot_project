package com.jonas.TechEventsRegistration.util.RequestsCreator;

import com.jonas.TechEventsRegistration.DTO.EventRequests.EventPostRequest;
import com.jonas.TechEventsRegistration.DTO.EventRequests.EventPutRequest;
import com.jonas.TechEventsRegistration.util.EventCreator;

public class EventPostAndPutCreator {
    public static EventPostRequest createEventPostRequest(){
        return EventPostRequest.builder()
                .eventName(EventCreator.createEvent().getEventName())
                .description(EventCreator.createEvent().getDescription())
                .eventDateAndHours(EventCreator.createEvent().getEventDateAndHours())
                .local(EventCreator.createEvent().getLocal())
                .category(EventCreator.createEvent().getCategory())
                .vacancies(EventCreator.createEvent().getVacancies())
                .build();
    }
    public static EventPutRequest createEventPutRequest(){
        return EventPutRequest.builder()
                .id(EventCreator.createEventUpdated().getId())
                .eventName(EventCreator.createEventUpdated().getEventName())
                .description(EventCreator.createEventUpdated().getDescription())
                .eventDateAndHours(EventCreator.createEventUpdated().getEventDateAndHours())
                .local(EventCreator.createEventUpdated().getLocal())
                .category(EventCreator.createEventUpdated().getCategory())
                .vacancies(EventCreator.createEventUpdated().getVacancies())
                .build();
    }
}
