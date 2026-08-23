package com.jonas.TechEventsRegistration.DTO.Event;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(example = "22/08/2026 14:30", type = "string")
    private LocalDateTime eventDateAndHours;
}
