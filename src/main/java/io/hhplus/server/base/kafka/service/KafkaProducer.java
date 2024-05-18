package io.hhplus.server.base.kafka.service;

import io.hhplus.server.domain.outbox.service.OutboxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final OutboxService outboxService;

    public void publish(String topic, Long outboxId, String jsonData) {
        log.info("sending jsonData={} to topic={}, at outboxId={}", jsonData, topic, outboxId);
        try {
            kafkaTemplate.send(topic, jsonData);
            outboxService.toPublished(outboxId);
        } catch (Exception e) {
            log.error("sending to topic={}, at outboxId={}, error = {}", topic, outboxId, e.getMessage());
            outboxService.toRetry(outboxId);
        }
    }
}
