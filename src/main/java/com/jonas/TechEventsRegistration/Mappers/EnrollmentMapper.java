package com.jonas.TechEventsRegistration.Mappers;

import com.jonas.TechEventsRegistration.DTO.EnrollmentRequests.EnrollmentPostRequest;
import com.jonas.TechEventsRegistration.DTO.EnrollmentRequests.EnrollmentPutRequest;
import com.jonas.TechEventsRegistration.Entity.Enrollment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EnrollmentMapper {

    @Mapping(target = "participant",ignore = true)
    @Mapping(target = "event",ignore = true)
    Enrollment enrollmentToPost (EnrollmentPostRequest enrollmentPostRequest);

    @Mapping(target = "participant",ignore = true)
    @Mapping(target = "event",ignore = true)
    void enrollmentToPut (EnrollmentPutRequest enrollmentPutRequest, @MappingTarget Enrollment enrollment);
}
