package com.jonas.TechEventsRegistration.Services;

import com.jonas.TechEventsRegistration.DTO.Event.EventRequest;
import com.jonas.TechEventsRegistration.DTO.Event.EventResponse;
import com.jonas.TechEventsRegistration.Entity.Event;
import com.jonas.TechEventsRegistration.Exceptions.NotFoundException;
import com.jonas.TechEventsRegistration.Exceptions.VacanciesLimitExceedException;
import com.jonas.TechEventsRegistration.Mappers.EventMapper;
import com.jonas.TechEventsRegistration.Repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class EventServices {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    public Page<EventResponse> findAll(Pageable pageable) {
        return eventRepository.findAll(pageable).map(eventMapper::toResponse);
    }

    public EventResponse findById(Long id) {
        return eventRepository.findById(id).map(eventMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Event not found with id: " + id));
    }

    public Event findEventEntityById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event not found with id: " + id));
    }

    public List<EventResponse> findByName(String name) {
        return eventRepository.findByEventNameContaining(name).stream().map(eventMapper::toResponse).toList();
    }

    @Transactional
    public EventResponse save(EventRequest eventRequest) {
        validateVacanciesMaxCapacity(eventRequest.getVacancies(), eventRequest.getMaxVacancies());
        Event entity = eventMapper.toEntity(eventRequest);
        eventRepository.save(entity);
        return eventMapper.toResponse(entity);
    }

    @Transactional
    public EventResponse update(Long id, EventRequest eventRequest) {
        findById(id);
        Event entity = eventMapper.toEntity(eventRequest);
        entity.setId(id);
        eventRepository.save(entity);
        return eventMapper.toResponse(entity);
    }

    @Transactional
    public void delete(Long id) {
        findById(id);
        eventRepository.deleteById(id);
    }
    private void validateVacanciesMaxCapacity(Integer vacancies, Integer maxVacancies){
        if (vacancies > maxVacancies){
            throw  new VacanciesLimitExceedException("vacancies cant not Exceed the limit");
        }
    }
}
