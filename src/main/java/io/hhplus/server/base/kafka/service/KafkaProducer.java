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

    public void publish(String topic, Long outboxId, String jsonData) {
        log.info("sending jsonData={} to topic={}, at outboxId={}", jsonData, topic, outboxId);
        kafkaTemplate.send(topic, String.valueOf(outboxId), jsonData);
    }
}
