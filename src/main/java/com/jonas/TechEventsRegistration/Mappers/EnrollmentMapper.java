package com.jonas.TechEventsRegistration.Mappers;

import com.jonas.TechEventsRegistration.DTO.Enrollment.EnrollmentRequest;
import com.jonas.TechEventsRegistration.DTO.Enrollment.EnrolmentResponse;
import com.jonas.TechEventsRegistration.Entity.Enrollment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {ParticipantMapper.class, EventMapper.class})
public interface EnrollmentMapper {

    Enrollment toEntity (EnrollmentRequest enrollmentRequest);

    EnrolmentResponse toResponse (Enrollment enrollment);

}
