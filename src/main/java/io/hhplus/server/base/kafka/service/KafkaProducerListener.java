package io.hhplus.server.base.kafka.service;

import io.hhplus.server.domain.outbox.entity.Outbox;
import io.hhplus.server.domain.outbox.service.OutboxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaProducerListener implements ProducerListener<String, String> {

    private final OutboxService outboxService;

    @Override
    public void onError(ProducerRecord producerRecord, RecordMetadata recordMetadata, Exception exception) {
        ProducerListener.super.onError(producerRecord, recordMetadata, exception);
        log.info("message body -> {}, message header -> {}, message topic -> {}, message offset -> {}",
                producerRecord.value(), producerRecord.headers(), recordMetadata.topic(), recordMetadata.offset());
        log.error("message exception -> {}", exception.getMessage());

        // 발행 실패 -> 재시도 필요 상태 마킹 :: RETRY
        String key = (String) producerRecord.key();
        Long sendId = Long.valueOf(key);
        Outbox outbox = outboxService.findById(sendId);
        outboxService.updateStatus(outbox, Outbox.Status.RETRY);
    }
}
