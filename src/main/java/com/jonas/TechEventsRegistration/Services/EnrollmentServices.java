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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EnrollmentServices {
    private final EnrollmentRepository enrollmentRepository;
    private final EnrollmentMapper enrollmentMapper;
    private final ParticipantServices participantServices;
    private final EventServices eventServices;


    public Page<Enrollment> findAll(Pageable pageable){
        return enrollmentRepository.findAll(pageable);
    }

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
        decreaseVacancies(event);
        return enrollmentRepository.save(enrollment);
    }
    @Transactional
    public Enrollment update(EnrollmentPutRequest enrollmentPutRequest){
        Event event = eventServices.findById(enrollmentPutRequest.getEventId());
        Participant participant = participantServices.findById(enrollmentPutRequest.getParticipantId());
        Enrollment enrollment = findById(enrollmentPutRequest.getId());

        if (!enrollment.getEvent().getId().equals(event.getId())){
            increaseVacancies(enrollment);
            decreaseVacancies(event);
        }

        enrollmentMapper.enrollmentToPut(enrollmentPutRequest, enrollment);
        enrollment.setParticipant(participant);
        enrollment.setEvent(event);
        return enrollmentRepository.save(enrollment);

    }
    @Transactional
    public void delete(Long id){
        Enrollment enrollment = findById(id);
        increaseVacancies(enrollment);
        enrollmentRepository.deleteById(id);
    }

    private void decreaseVacancies(Event event){
        if (event.getVacancies() <= 0){
            throw  new IllegalStateException("No vacancies dispo for this event");
        }
        event.setVacancies(event.getVacancies() - 1);
    }
    private void increaseVacancies(Enrollment enrollment){
        enrollment.getEvent().setVacancies(
                enrollment.getEvent().getVacancies() + 1
        );
    }
}
