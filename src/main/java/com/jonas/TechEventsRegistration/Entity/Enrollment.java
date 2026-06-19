package com.jonas.TechEventsRegistration.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Enrollment {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Participant participant;
    @ManyToOne
    private Event event;

    @JoinColumn (name = "enrollment_date")
    @JsonFormat (pattern = "dd/MM/yyyy")
    private LocalDate enrollmentDate;
}
