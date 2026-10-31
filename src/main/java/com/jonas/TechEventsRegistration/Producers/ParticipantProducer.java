package com.jonas.TechEventsRegistration.Producers;

import com.jonas.TechEventsRegistration.DTO.EmailRabbitMQ.ParticipantEmailRequest;
import com.jonas.TechEventsRegistration.Entity.Participant;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Component
@RequiredArgsConstructor
public class ParticipantProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value(value = "${broker.queue.participant-created}")
    private String routingKey;



    public void publishMessageEmail(Participant participant){
        ParticipantEmailRequest emailRequest = new ParticipantEmailRequest();
        emailRequest.setParticipantId(participant.getId());
        emailRequest.setEmailTo(participant.getEmail());
        emailRequest.setSubject("Cadastro no TechEvents.com realizado com sucesso!!!!");
        emailRequest.setText("Seja muito bem vindo(a), você ganhou 1 milhão de reais!!!!");

        rabbitTemplate.convertAndSend("", routingKey, emailRequest);
    }
}
