package com.jonas.TechEventsRegistration.Repository;

import com.jonas.TechEventsRegistration.Entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByEventNameContaining(String EventName);
}
