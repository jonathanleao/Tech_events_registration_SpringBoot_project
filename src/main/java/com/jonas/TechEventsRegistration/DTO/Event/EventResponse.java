package com.jonas.TechEventsRegistration.DTO.Event;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventResponse {
    private String eventName;
    private String description;
    private String local;
    private String category;
    private LocalDateTime eventDateAndHours;
}
