package com.jonas.TechEventsRegistration.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String eventName;
    private String description;
    private String local;
    private String category;
    private Integer vacancies;

    @JoinColumn (name = "event_date_and_hours")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime eventDateAndHours;
}
