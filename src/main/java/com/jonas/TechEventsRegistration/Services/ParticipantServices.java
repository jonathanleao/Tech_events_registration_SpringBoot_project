package com.jonas.TechEventsRegistration.Services;

import com.jonas.TechEventsRegistration.DTO.Participant.ParticipantRequest;
import com.jonas.TechEventsRegistration.DTO.Participant.ParticipantResponse;
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

    public Page<ParticipantResponse> findAll(Pageable pageable){
        return participantRepository.findAll(pageable).map(participantMapper::toResponse);
    }

    public ParticipantResponse findById(Long id) {
        return participantRepository.findById(id).map(participantMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Participant not found with id: "+ id));
    }

    public Participant findParticipantEntityById(Long id){
        return participantRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Participant not found with id: "+ id));
    }

    public List<ParticipantResponse> findByName (String name){
        return participantRepository.findByParticipantNameContaining(name)
                .stream().map(participantMapper::toResponse).toList();
    }

    @Transactional
    public ParticipantResponse save(ParticipantRequest participantRequest) {
        Participant entity = participantMapper.toEntity(participantRequest);
        Participant entitySaved = participantRepository.save(entity);
        return participantMapper.toResponse(entitySaved);
    }

    @Transactional
    public ParticipantResponse update(Long id,ParticipantRequest participantRequest ) {
       findById(id);
        Participant entity = participantMapper.toEntity(participantRequest);
        entity.setId(id);
        Participant entitySaved = participantRepository.save(entity);
        return participantMapper.toResponse(entitySaved);
    }

    @Transactional
    public void delete(Long id) {
        findParticipantEntityById(id);
        participantRepository.deleteById(id);
    }
}
