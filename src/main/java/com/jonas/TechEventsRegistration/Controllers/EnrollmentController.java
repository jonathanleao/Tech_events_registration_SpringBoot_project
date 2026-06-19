package com.jonas.TechEventsRegistration.Controllers;

import com.jonas.TechEventsRegistration.DTO.EnrollmentRequests.EnrollmentPostRequest;
import com.jonas.TechEventsRegistration.DTO.EnrollmentRequests.EnrollmentPutRequest;
import com.jonas.TechEventsRegistration.Entity.Enrollment;
import com.jonas.TechEventsRegistration.Services.EnrollmentServices;
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

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<Enrollment>> findAll(Pageable pageable){
        return new ResponseEntity<>(enrollmentServices.findAll(pageable), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Enrollment> findById(@PathVariable Long id){
        return  new ResponseEntity<>(enrollmentServices.findById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Enrollment> save (@RequestBody EnrollmentPostRequest enrollmentPostRequest){
        return  new ResponseEntity<>(enrollmentServices.save(enrollmentPostRequest), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Enrollment> update(@RequestBody EnrollmentPutRequest enrollmentPutRequest){
        return new ResponseEntity<>(enrollmentServices.update(enrollmentPutRequest),HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        enrollmentServices.delete(id);
        return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
