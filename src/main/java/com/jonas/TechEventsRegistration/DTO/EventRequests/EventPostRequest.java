package com.jonas.TechEventsRegistration.DTO.EventRequests;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventPostRequest {
    private String eventName;
    private String description;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime eventDateAndHours;

    private String local;
    private String category;
    private Integer vacancies;
}
