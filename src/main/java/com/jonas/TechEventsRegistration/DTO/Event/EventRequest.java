package com.jonas.TechEventsRegistration.DTO.Event;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventRequest {

    @NotBlank
    private String eventName;
    @NotBlank
    private String description;
    @NotBlank
    private String local;
    @NotBlank
    private String category;
    @NotNull
    @Min(1)
    private Integer vacancies;
    @NotNull
    private Integer maxVacancies;

    @NotNull
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime eventDateAndHours;
}
