package com.jonas.TechEventsRegistration.Controllers;

import com.jonas.TechEventsRegistration.DTO.Enrollment.EnrollmentRequest;
import com.jonas.TechEventsRegistration.DTO.Enrollment.EnrollmentResponse;
import com.jonas.TechEventsRegistration.Services.EnrollmentServices;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "Enrollments")
public class EnrollmentController {

    private final EnrollmentServices enrollmentServices;

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<EnrollmentResponse>> findAll(@Parameter (hidden = true) Pageable pageable){
        return new ResponseEntity<>(enrollmentServices.findAll(pageable), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<EnrollmentResponse> findById(@PathVariable Long id){
        return  new ResponseEntity<>(enrollmentServices.findById(id), HttpStatus.OK);
    }

    @PostMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EnrollmentResponse> save (@Valid @RequestBody EnrollmentRequest enrollmentRequest){
        return  new ResponseEntity<>(enrollmentServices.save(enrollmentRequest), HttpStatus.CREATED);
    }

    @PutMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EnrollmentResponse> update(@PathVariable Long id, @Valid @RequestBody EnrollmentRequest enrollmentRequest){
        return new ResponseEntity<>(enrollmentServices.update(id, enrollmentRequest),HttpStatus.OK);
    }

    @DeleteMapping(path = "/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        enrollmentServices.delete(id);
        return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
