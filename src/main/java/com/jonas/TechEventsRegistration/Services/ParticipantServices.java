package com.jonas.TechEventsRegistration.Services;

import com.jonas.TechEventsRegistration.DTO.ParticipantRequest.ParticipantPostRequest;
import com.jonas.TechEventsRegistration.DTO.ParticipantRequest.ParticipantPutRequest;
import com.jonas.TechEventsRegistration.Entity.Participant;
import com.jonas.TechEventsRegistration.Exceptions.NotFoundException;
import com.jonas.TechEventsRegistration.Mappers.ParticipantMapper;
import com.jonas.TechEventsRegistration.Repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParticipantServices {
    private final ParticipantRepository participantRepository;
    private final ParticipantMapper participantMapper;

    public Page<Participant> findAll(Pageable pageable){
        return participantRepository.findAll(pageable);
    }

    public Participant findById(Long id) {
        return participantRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Id not found"));
    }

    public List<Participant> findByName (String name){
        return  participantRepository.findByParticipantNameContaining(name);
    }

    @Transactional
    public Participant save(ParticipantPostRequest participantPostRequest) {
        Participant participant = participantMapper.participantToPost(participantPostRequest);
        return participantRepository.save(participant);
    }

    @Transactional
    public Participant update(ParticipantPutRequest participantPutRequest) {
        Participant participant = findById(participantPutRequest.getId());
        participantMapper.participantToPut(participantPutRequest, participant);
        return participantRepository.save(participant);
    }

    @Transactional
    public void delete(Long id) {
        findById(id);
        participantRepository.deleteById(id);
    }
}
