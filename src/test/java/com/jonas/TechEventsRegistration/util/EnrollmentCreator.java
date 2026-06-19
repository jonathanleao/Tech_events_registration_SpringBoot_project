package com.jonas.TechEventsRegistration.util;

import com.jonas.TechEventsRegistration.Entity.Enrollment;

import java.time.LocalDate;

public class EnrollmentCreator {
    public static Enrollment enrollmentCreator(){
        return Enrollment.builder()
                .event(EventCreator.createEventValid())
                .participant(ParticipantCreator.createParticipantValid())
                .enrollmentDate(LocalDate.of(2026, 1, 23))
                .build();
    }
    public static Enrollment enrollmentCreatorValid(){
        return Enrollment.builder()
                .id(1L)
                .event(EventCreator.createEventValid())
                .participant(ParticipantCreator.createParticipantValid())
                .enrollmentDate(LocalDate.of(2026, 1, 23))
                .build();
    }
    public static Enrollment enrollmentCreatorUpdated(){
        return Enrollment.builder()
                .id(enrollmentCreatorValid().getId())
                .event(EventCreator.createEventValid())
                .participant(ParticipantCreator.createParticipantValid())
                .enrollmentDate(LocalDate.of(2026, 3, 10))
                .build();
    }
}
