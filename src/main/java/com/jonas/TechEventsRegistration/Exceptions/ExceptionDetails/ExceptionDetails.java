package com.jonas.TechEventsRegistration.Exceptions.ExceptionDetails;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ExceptionDetails {
    private String title;
    private Integer status;
    private LocalDateTime timestamp;
    private String message;
}
