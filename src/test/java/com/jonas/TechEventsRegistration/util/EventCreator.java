package com.jonas.TechEventsRegistration.util;

import com.jonas.TechEventsRegistration.Entity.Event;
import com.jonas.TechEventsRegistration.Entity.Participant;

import java.time.LocalDateTime;

public class EventCreator {
    public static Event createEvent(){
        return Event.builder()
                .eventName("Programação Orientada a Objeto em Java")
                .local("Rua Rio Madeira, Iranduba-AM")
                .category("Tecnologia e Programação")
                .description("Evento para aprendizado e Programção Orientada a Objetos para estudantes" +
                        " de áreas da tecnologia usando a linguagem em java")
                .eventDateAndHours(LocalDateTime.of(2026, 1, 23, 10, 0))
                .vacancies(50).build();
    }
    public static Event createEventValid(){
        return Event.builder()
                .id(1L)
                .eventName("Programação Orientada a Objeto em Java")
                .local("Rua Rio Madeira, Iranduba-AM")
                .category("Tecnologia e Programação")
                .description("Evento para aprendizado e Programção Orientada a Objetos para estudantes" +
                        " de áreas da tecnologia usando a linguagem Java")
                .eventDateAndHours(LocalDateTime.of(2026, 1, 23, 10, 0))
                .vacancies(50).build();
    }
    public static Event createEventUpdated(){
        return Event.builder()
                .id(createEventValid().getId())
                .eventName("Programação Orientada a Objeto em Python")
                .local("Rua Rio Madeira, Iranduba-AM")
                .category("Tecnologia e Programação")
                .description("Evento para aprendizado e Programção Orientada a Objetos para estudantes" +
                        " de áreas da tecnologia usando a Linguagem python")
                .eventDateAndHours(LocalDateTime.of(2026, 1, 23, 10, 0))
                .vacancies(50).build();
    }
}
