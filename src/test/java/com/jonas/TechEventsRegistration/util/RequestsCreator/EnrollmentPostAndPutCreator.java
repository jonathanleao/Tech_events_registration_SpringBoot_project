package com.jonas.TechEventsRegistration.util.RequestsCreator;

import com.jonas.TechEventsRegistration.DTO.EnrollmentRequests.EnrollmentPostRequest;
import com.jonas.TechEventsRegistration.DTO.EnrollmentRequests.EnrollmentPutRequest;
import com.jonas.TechEventsRegistration.util.EnrollmentCreator;

public class EnrollmentPostAndPutCreator {
    public static EnrollmentPostRequest createEnrollmentPostRequest(){
        return EnrollmentPostRequest.builder()
                .participantId(EnrollmentCreator.enrollmentCreator().getParticipant().getId())
                .eventId(EnrollmentCreator.enrollmentCreator().getEvent().getId())
                .enrollmentDate(EnrollmentCreator.enrollmentCreator().getEnrollmentDate())
                .build();
    }
    public static EnrollmentPutRequest createEnrollmentPutRequest(){
        return EnrollmentPutRequest.builder()
                .id(EnrollmentCreator.enrollmentCreatorUpdated().getId())
                .participantId(EnrollmentCreator.enrollmentCreatorUpdated().getParticipant().getId())
                .eventId(EnrollmentCreator.enrollmentCreatorUpdated().getEvent().getId())
                .enrollmentDate(EnrollmentCreator.enrollmentCreatorUpdated().getEnrollmentDate())
                .build();
    }
}
