package com.jonas.TechEventsRegistration.Controllers;

import com.jonas.TechEventsRegistration.DTO.Event.EventRequest;
import com.jonas.TechEventsRegistration.DTO.Event.EventResponse;
import com.jonas.TechEventsRegistration.Services.EventServices;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
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
    public ResponseEntity<Page<EventResponse>> findAll(@Parameter(hidden = true) Pageable pageable) {
        return new ResponseEntity<>(eventServices.findAll(pageable), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<EventResponse> findById(@PathVariable Long id) {
        return new ResponseEntity<>(eventServices.findById(id), HttpStatus.OK);
    }

    @GetMapping(path = "/find")
    public ResponseEntity<List<EventResponse>> findByName(@RequestParam String name) {
        return new ResponseEntity<>(eventServices.findByName(name), HttpStatus.OK);
    }

    @PostMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EventResponse> save(@RequestBody EventRequest eventRequest) {
        return new ResponseEntity<>(eventServices.save(eventRequest), HttpStatus.CREATED);
    }

    @PutMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EventResponse> update(@PathVariable Long id, @Valid @RequestBody EventRequest eventRequest) {
        return new ResponseEntity<>(eventServices.update(id, eventRequest), HttpStatus.OK);
    }

    @DeleteMapping(path = "/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        eventServices.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
