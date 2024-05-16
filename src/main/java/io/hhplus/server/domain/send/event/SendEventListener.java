package io.hhplus.server.domain.send.event;

import io.hhplus.server.base.kafka.KafkaConstants;
import io.hhplus.server.base.kafka.service.KafkaProducer;
import io.hhplus.server.domain.send.dto.SendCommReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class SendEventListener {

    private final KafkaProducer kafkaProducer;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onSendEvent(SendEvent event) {
        SendCommReqDto sendReqDto = event.getSendReqDto();
        // Kafka 메시지 발행
        kafkaProducer.send(KafkaConstants.RESERVATION_TOPIC, String.valueOf(sendReqDto.getSendId()), sendReqDto.getJsonData());
    }
}
