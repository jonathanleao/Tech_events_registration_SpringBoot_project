package com.jonas.TechEventsRegistration.Exceptions;

public class VacanciesLimitExceedException extends RuntimeException {
    public VacanciesLimitExceedException(String message) {
        super(message);
    }
}
