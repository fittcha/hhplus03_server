package io.hhplus.server.base.kafka.service;

import io.hhplus.server.base.kafka.KafkaConstants;
import io.hhplus.server.domain.outbox.entity.Outbox;
import io.hhplus.server.domain.outbox.service.OutboxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxRetryScheduler {

    private final OutboxService outboxService;
    private final KafkaProducer kafkaProducer;

    @Scheduled(fixedDelay = 600000) // 5분마다 실행
    public void retryFailedMessages() {
        // Outbox 테이블에서 재시도가 필요한 메시지 조회
        List<Outbox> outboxesToRetry = outboxService.findAllByStatus(Outbox.Status.RETRY);
        if (outboxesToRetry.isEmpty()) {
            return;
        }

        for (Outbox outbox : outboxesToRetry) {
            if (outbox.getRetryCount() >= 3) {
                // 이미 3회 이상 재시도한 메시지 실패 처리
                outboxService.updateStatus(outbox, Outbox.Status.FAIL);
                continue;
            }
            try {
                // 재시도 로직, KafkaProducer 메시지 재발행
                String topic = null;
                switch (outbox.getType()) {
                    case SEND -> topic = KafkaConstants.SEND_TOPIC;
                    case PAYMENT_REFUND -> topic = KafkaConstants.PAYMENT_REFUND_TOPIC;
                    case SEAT_STATUS -> topic = KafkaConstants.SEAT_STATUS_TOPIC;
                }

                kafkaProducer.publish(topic, outbox.getOutboxId(), outbox.getJsonData());

            } catch (Exception e) {
                log.error("send retry exception -> sendId: {}, error: {}", outbox.getOutboxId(), e.getMessage());
            }
            // 재시도 횟수 추가
            outbox.plusRetryCount();
        }
    }
}
