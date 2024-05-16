package io.hhplus.server.base.kafka.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void send(String topic, String sendId, String message) {
        log.info("sending message={} to topic={}, at sendId={}", message, topic, sendId);
        kafkaTemplate.send(topic, sendId, message);
    }
}
