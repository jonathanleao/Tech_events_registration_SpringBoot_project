package com.jonas.TechEventsRegistration.Producers;


import com.jonas.TechEventsRegistration.DTO.EmailRabbitMQ.EmailRequest;
import com.jonas.TechEventsRegistration.Entity.Enrollment;
import com.jonas.TechEventsRegistration.Entity.Participant;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Component
@RequiredArgsConstructor
public class EnrollmentProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value(value = "${broker.queue.enrollment-created}")
    private String routingKey;

    public void publishMessageEmail(Enrollment enrollment){
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setParticipantId(enrollment.getParticipant().getId());
        emailRequest.setEmailTo(enrollment.getParticipant().getEmail());
        emailRequest.setSubject("Inscrição Realizada com sucesso no evento: " + enrollment.getEvent().getEventName()+"!!!!!");
        emailRequest.setText("Acompanhe a data do evento para não se atrasar bom proveito!!!!");

        rabbitTemplate.convertAndSend("", routingKey, emailRequest);
    }
}
