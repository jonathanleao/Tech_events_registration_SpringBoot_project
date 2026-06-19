package com.jonas.TechEventsRegistration.Controllers;

import com.jonas.TechEventsRegistration.DTO.ParticipantRequest.ParticipantPostRequest;
import com.jonas.TechEventsRegistration.DTO.ParticipantRequest.ParticipantPutRequest;
import com.jonas.TechEventsRegistration.Entity.Participant;
import com.jonas.TechEventsRegistration.Services.ParticipantServices;
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
@RequestMapping(path = "Participants")
public class ParticipantController {

    private final ParticipantServices participantServices;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<Participant>> findAll(Pageable pageable) {
        return new ResponseEntity<>(participantServices.findAll(pageable), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Participant> findById(@PathVariable Long id) {
        return new ResponseEntity<>(participantServices.findById(id), HttpStatus.OK);
    }

    @GetMapping(path = "/find")
    public ResponseEntity<List<Participant>> findByName(@RequestParam String name) {
        return new ResponseEntity<>(participantServices.findByName(name), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Participant> save(@RequestBody ParticipantPostRequest participantPostRequest) {
        return new ResponseEntity<>(participantServices.save(participantPostRequest), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Participant> update(@RequestBody ParticipantPutRequest participantPutRequest) {
        return new ResponseEntity<>(participantServices.update(participantPutRequest), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        participantServices.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

