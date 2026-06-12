package com.jonas.TechEventsRegistration.Services;

import com.jonas.TechEventsRegistration.DTO.EnrollmentRequests.EnrollmentPostRequest;
import com.jonas.TechEventsRegistration.DTO.EnrollmentRequests.EnrollmentPutRequest;
import com.jonas.TechEventsRegistration.Entity.Enrollment;
import com.jonas.TechEventsRegistration.Entity.Event;
import com.jonas.TechEventsRegistration.Entity.Participant;
import com.jonas.TechEventsRegistration.Exceptions.NotFoundException;
import com.jonas.TechEventsRegistration.Mappers.EnrollmentMapper;
import com.jonas.TechEventsRegistration.Repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EnrollmentServices {
    private final EnrollmentRepository enrollmentRepository;
    private final EnrollmentMapper enrollmentMapper;
    private final ParticipantServices participantServices;
    private final EventServices eventServices;

    public Enrollment findById(Long id){
        return enrollmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Id not Found"));
    }

    @Transactional
    public Enrollment save (EnrollmentPostRequest enrollmentPostRequest){
        Participant participant = participantServices.findById(enrollmentPostRequest.getParticipantId());
        Event event= eventServices.findById(enrollmentPostRequest.getEventId());
        Enrollment enrollment = enrollmentMapper.enrollmentToPost(enrollmentPostRequest);
        enrollment.setEvent(event);
        enrollment.setParticipant(participant);
        return enrollmentRepository.save(enrollment);
    }
    @Transactional
    public Enrollment update(EnrollmentPutRequest enrollmentPutRequest){
        Event event = eventServices.findById(enrollmentPutRequest.getEventId());
        Participant participant = participantServices.findById(enrollmentPutRequest.getParticipantId());
        Enrollment enrollment = findById(enrollmentPutRequest.getId());
        enrollmentMapper.enrollmentToPut(enrollmentPutRequest, enrollment);
        enrollment.setParticipant(participant);
        enrollment.setEvent(event);
        return enrollmentRepository.save(enrollment);

    }

    public void delete(Long id){
        findById(id);
        enrollmentRepository.deleteById(id);
    }
}
