package com.jonas.TechEventsRegistration.DTO.EventRequests;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventPutRequest {
    @NotNull
    private Long id;
    @NotBlank
    private String eventName;
    @NotBlank
    private String description;

    @NotNull
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime eventDateAndHours;

    @NotBlank
    private String local;
    @NotBlank
    private String category;
    @NotNull
    private Integer vacancies;
}
