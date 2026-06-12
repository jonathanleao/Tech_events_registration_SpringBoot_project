package com.jonas.TechEventsRegistration.Repository;

import com.jonas.TechEventsRegistration.Entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    List<Participant> findByParticipantNameContaining(String EventName);
}
