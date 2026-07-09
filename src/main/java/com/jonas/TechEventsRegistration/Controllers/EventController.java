package com.jonas.TechEventsRegistration.Controllers;

import com.jonas.TechEventsRegistration.DTO.EventRequests.EventPostRequest;
import com.jonas.TechEventsRegistration.DTO.EventRequests.EventPutRequest;
import com.jonas.TechEventsRegistration.Entity.Event;
import com.jonas.TechEventsRegistration.Services.EventServices;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "Events")
public class EventController {

    private final EventServices eventServices;

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<Event>> findAll(@Parameter(hidden = true) Pageable pageable) {
        return new ResponseEntity<>(eventServices.findAll(pageable), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Event> findById(@PathVariable Long id) {
        return new ResponseEntity<>(eventServices.findById(id), HttpStatus.OK);
    }

    @GetMapping(path = "/find")
    public ResponseEntity<List<Event>> findByName(@RequestParam String name) {
        return new ResponseEntity<>(eventServices.findByName(name), HttpStatus.OK);
    }

    @PostMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Event> save(@RequestBody EventPostRequest eventPostRequest) {
        return new ResponseEntity<>(eventServices.save(eventPostRequest), HttpStatus.CREATED);
    }

    @PutMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Event> update(@RequestBody EventPutRequest eventPutRequest) {
        return new ResponseEntity<>(eventServices.update(eventPutRequest), HttpStatus.OK);
    }

    @DeleteMapping(path = "/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        eventServices.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
