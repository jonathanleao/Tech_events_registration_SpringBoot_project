package com.jonas.TechEventsRegistration.Services;

import com.jonas.TechEventsRegistration.DTO.Enrollment.EnrollmentRequest;
import com.jonas.TechEventsRegistration.DTO.Enrollment.EnrollmentResponse;
import com.jonas.TechEventsRegistration.Entity.Enrollment;
import com.jonas.TechEventsRegistration.Entity.Event;
import com.jonas.TechEventsRegistration.Entity.Participant;
import com.jonas.TechEventsRegistration.Exceptions.NoVacanciesAvailableException;
import com.jonas.TechEventsRegistration.Exceptions.NotFoundException;
import com.jonas.TechEventsRegistration.Exceptions.VacanciesLimitExceedException;
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


    public Page<EnrollmentResponse> findAll(Pageable pageable){
        return enrollmentRepository.findAll(pageable).map(enrollmentMapper::toResponse);
    }

    public EnrollmentResponse findById(Long id){
        return enrollmentRepository.findById(id).map(enrollmentMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("enrollment not Found whit id: " + id));
    }

    public Enrollment findEnrollmentEntityById(Long id){
        return enrollmentRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Enrollment not found with id: " + id));
    }

    @Transactional
    public EnrollmentResponse save (EnrollmentRequest enrollmentRequest){
        Event event = eventServices.findEventEntityById(enrollmentRequest.getEventId());
        Participant participant = participantServices.findParticipantEntityById(enrollmentRequest.getParticipantId());
        Enrollment entity = enrollmentMapper.toEntity(enrollmentRequest);
        entity.setEvent(event);
        entity.setParticipant(participant);
        decreaseVacancies(event);
        Enrollment entitySaved = enrollmentRepository.save(entity);
        return enrollmentMapper.toResponse(entitySaved);
    }
    @Transactional
    public EnrollmentResponse update(Long id, EnrollmentRequest enrollmentRequest){
        Enrollment enrollment = findEnrollmentEntityById(id);
        Event event = eventServices.findEventEntityById(enrollmentRequest.getEventId());
        Participant participant = participantServices.findParticipantEntityById(enrollmentRequest.getParticipantId());

        if (!enrollment.getEvent().getId().equals(event.getId())){
            increaseVacancies(enrollment.getEvent());
            decreaseVacancies(event);
        }

        enrollment.setEvent(event);
        enrollment.setParticipant(participant);
        Enrollment entitySaved = enrollmentRepository.save(enrollment);
        return enrollmentMapper.toResponse(entitySaved);

    }
    @Transactional
    public void delete(Long id){
        Enrollment enrollment = findEnrollmentEntityById(id);
        increaseVacancies(enrollment.getEvent());
        enrollmentRepository.deleteById(id);
    }

    private void decreaseVacancies(Event event){
        if (event.getVacancies() <= 0){
            throw  new NoVacanciesAvailableException("No vacancies dispo for this event");
        }
        event.setVacancies(event.getVacancies() - 1);
    }
    private void increaseVacancies(Event event){
        Integer newVacancies = event.getVacancies() + 1;
        validateVacanciesMaxCapacity(newVacancies, event.getMaxVacancies());
        event.setVacancies(newVacancies);
    }
    private void validateVacanciesMaxCapacity(Integer vacancies, Integer maxVacancies){
        if (vacancies > maxVacancies){
            throw  new VacanciesLimitExceedException("vacancies cant not exceed the limit");
        }
    }

}
