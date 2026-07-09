package com.jonas.TechEventsRegistration.ExceptionsHandler;

import com.jonas.TechEventsRegistration.Exceptions.BadRequestException;
import com.jonas.TechEventsRegistration.Exceptions.ExceptionDetails.ExceptionDetails;
import com.jonas.TechEventsRegistration.Exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ExceptionsHandler {
    @ExceptionHandler (NotFoundException.class)
    ResponseEntity<ExceptionDetails> handlerNotFoundException(NotFoundException not){
        return new ResponseEntity<>(
                ExceptionDetails.builder()
                        .title("Not Found Exception")
                        .message(not.getMessage())
                        .status(HttpStatus.NOT_FOUND.value())
                        .timestamp(LocalDateTime.now())
                        .build(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler (BadRequestException.class)
    ResponseEntity<ExceptionDetails> handlerBadRequestException(BadRequestException bre){
        return new ResponseEntity<>(
                ExceptionDetails.builder()
                        .title("Bad Request Exception")
                        .message(bre.getMessage())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .timestamp(LocalDateTime.now())
                        .build(), HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(Exception.class)
    ResponseEntity<ExceptionDetails> handlerGenericException(Exception e){
        return new ResponseEntity<>(
                ExceptionDetails.builder()
                        .title("Internal Server Error")
                        .message("An unexpected error occurred")
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .timestamp(LocalDateTime.now())
                        .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
