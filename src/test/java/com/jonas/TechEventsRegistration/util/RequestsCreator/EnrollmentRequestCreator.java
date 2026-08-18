package com.jonas.TechEventsRegistration.util.RequestsCreator;

import com.jonas.TechEventsRegistration.DTO.Enrollment.EnrollmentRequest;

import java.time.LocalDate;

public class EnrollmentRequestCreator {

    public static EnrollmentRequest createEnrollmentRequest(){
        return EnrollmentRequest.builder()
                .participantId(1L)
                .eventId(1L)
                .enrollmentDate(LocalDate.of(2026,1,23))
                .build();
    }
    public static EnrollmentRequest createEnrollmentRequestUpdated(){
        return EnrollmentRequest.builder()
                .participantId(1L)
                .eventId(1L)
                .enrollmentDate(LocalDate.of(2026,3,10))
                .build();
    }
}
