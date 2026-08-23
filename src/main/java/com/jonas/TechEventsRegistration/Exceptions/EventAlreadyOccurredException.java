package com.jonas.TechEventsRegistration.Exceptions;

public class EventAlreadyOccurredException extends RuntimeException {
    public EventAlreadyOccurredException(String message) {
        super(message);
    }
}
