package io.hhplus.server.base.kafka.service;

import io.hhplus.server.base.kafka.KafkaConstants;
import io.hhplus.server.domain.send.entity.Send;
import io.hhplus.server.domain.send.service.SendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaRetryScheduler {

    private final SendService sendService;
    private final KafkaProducer kafkaProducer;

    @Scheduled(fixedDelay = 600000) // 5분마다 실행
    public void retryFailedMessages() {
        // Outbox 테이블에서 재시도가 필요한 메시지 조회
        List<Send> sendsToRetry = sendService.findAllByStatus(Send.Status.RETRY);
        if (sendsToRetry.isEmpty()) {
            return;
        }

        for (Send send : sendsToRetry) {
            if (send.getRetryCount() >= 3) {
                // 이미 3회 이상 재시도한 메시지 실패 처리
                sendService.updateStatus(send, Send.Status.FAIL);
                continue;
            }
            try {
                // 재시도 로직, KafkaProducer 메시지 재발행
                kafkaProducer.send(KafkaConstants.RESERVATION_TOPIC, String.valueOf(send.getSendId()), send.getJsonData());
            } catch (Exception e) {
                log.error("send retry exception -> sendId: {}, error: {}", send.getSendId(), e.getMessage());
            }
            // 재시도 횟수 추가
            send.plusRetryCount();
        }
    }
}
