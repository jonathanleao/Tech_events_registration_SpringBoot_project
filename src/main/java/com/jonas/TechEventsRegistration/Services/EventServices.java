package com.jonas.TechEventsRegistration.Services;

import com.jonas.TechEventsRegistration.DTO.EventRequests.EventPostRequest;
import com.jonas.TechEventsRegistration.DTO.EventRequests.EventPutRequest;
import com.jonas.TechEventsRegistration.Entity.Event;
import com.jonas.TechEventsRegistration.Exceptions.NotFoundException;
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

    public Page<Event> findAll(Pageable pageable){
        return eventRepository.findAll(pageable);
    }

    public Event findById(Long id){
        return eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Id not Found"));
    }

    public List <Event> findByName(String name){
        return eventRepository.findByEventNameContaining(name);
    }

    @Transactional
    public Event save (EventPostRequest eventPostRequest){
        Event event = eventMapper.eventToPost(eventPostRequest);
        return eventRepository.save(event);
    }
    @Transactional
    public Event update(EventPutRequest eventPutRequest){
        Event event = findById(eventPutRequest.getId());
        eventMapper.eventToPut(eventPutRequest,event);
        return eventRepository.save(event);
    }
    public void delete(Long id){
        findById(id);
        eventRepository.deleteById(id);
    }
}
