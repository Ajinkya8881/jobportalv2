package com.jobportal.jobportalv2.event;


import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApplicationEventProducer {

    private final KafkaTemplate<String, ApplicationEvent> kafkaTemplate;

    private static final String TOPIC = "application-events";

    public void publishEvent(ApplicationEvent event) {
        kafkaTemplate.send(TOPIC, event);
    }
}
