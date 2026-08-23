package com.jonas.TechEventsRegistration.Exceptions;

public class NoVacanciesAvailableException extends RuntimeException {
    public NoVacanciesAvailableException(String message) {
        super(message);
    }
}
