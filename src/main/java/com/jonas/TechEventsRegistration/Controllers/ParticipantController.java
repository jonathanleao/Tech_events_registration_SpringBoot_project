package com.jonas.TechEventsRegistration.Controllers;

import com.jonas.TechEventsRegistration.DTO.Participant.ParticipantRequest;
import com.jonas.TechEventsRegistration.DTO.Participant.ParticipantResponse;
import com.jonas.TechEventsRegistration.Services.ParticipantServices;
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
@RequestMapping(path = "Participants")
public class ParticipantController {

    private final ParticipantServices participantServices;


    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<ParticipantResponse>> findAll(@Parameter(hidden = true) Pageable pageable) {
        return new ResponseEntity<>(participantServices.findAll(pageable), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ParticipantResponse> findById(@PathVariable Long id) {
        return new ResponseEntity<>(participantServices.findById(id), HttpStatus.OK);
    }

    @GetMapping(path = "/find")
    public ResponseEntity<List<ParticipantResponse>> findByName(@RequestParam String name) {
        return new ResponseEntity<>(participantServices.findByName(name), HttpStatus.OK);
    }

    @PostMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ParticipantResponse> save(@RequestBody ParticipantRequest participantRequest) {
        return new ResponseEntity<>(participantServices.save(participantRequest), HttpStatus.CREATED);
    }

    @PutMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ParticipantResponse> update(@PathVariable Long id, @Valid @RequestBody ParticipantRequest participantPutRequest) {
        return new ResponseEntity<>(participantServices.update(id, participantPutRequest), HttpStatus.OK);
    }

    @DeleteMapping(path = "/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        participantServices.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

