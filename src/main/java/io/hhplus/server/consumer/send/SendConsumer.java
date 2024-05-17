package io.hhplus.server.consumer.send;

import io.hhplus.server.base.kafka.KafkaConstants;
import io.hhplus.server.domain.send.event.SendEvent;
import io.hhplus.server.domain.outbox.entity.Outbox;
import io.hhplus.server.domain.outbox.service.OutboxService;
import io.hhplus.server.domain.send.service.SendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SendConsumer {

    private final SendService sendService;
    private final OutboxService outboxService;

    @KafkaListener(topics = KafkaConstants.SEND_TOPIC, groupId = "hhplus-01")
    public void send(String outboxId, String message) {
        try {
            log.info("Received Message: {}", message);

            Outbox outbox = outboxService.findById(Long.valueOf(outboxId));
            // Outbox update :: PUBLISHED
            outboxService.updateStatus(outbox, Outbox.Status.PUBLISHED);

            // send to DataPlatform
            SendEvent sendEvent = new SendEvent(Long.valueOf(outboxId), message);
            boolean isSuccess = sendService.send(sendEvent);

            if (isSuccess) {
                // Outbox table update : SUCCESS
                outbox.updateStatus(Outbox.Status.SUCCESS);
                log.info("Message processed successfully");
            }
        } catch (Exception e) {
            log.error("Failed to process message: {}", e.getMessage());
        }
    }
}
