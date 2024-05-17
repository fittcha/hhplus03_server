package io.hhplus.server.consumer.payment;

import io.hhplus.server.base.kafka.KafkaConstants;
import io.hhplus.server.domain.outbox.entity.Outbox;
import io.hhplus.server.domain.outbox.service.OutboxService;
import io.hhplus.server.domain.payment.service.PaymentService;
import io.hhplus.server.domain.payment.service.dto.CancelPaymentResultResDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentConsumer {

    private final PaymentService paymentService;
    private final OutboxService outboxService;

    @KafkaListener(topics = KafkaConstants.PAYMENT_REFUND_TOPIC, groupId = "hhplus-01")
    public void refund(String outboxId, String paymentId) {
        try {
            log.info("Received PAYMENT_REFUND_TOPIC: {}", outboxId);

            Outbox outbox = outboxService.findById(Long.valueOf(outboxId));
            // Outbox update :: PUBLISHED
            outboxService.updateStatus(outbox, Outbox.Status.PUBLISHED);

            // payment refund
            CancelPaymentResultResDto cancel = paymentService.cancel(paymentId != null ? Long.valueOf(paymentId) : null);

            if (cancel.isSuccess()) {
                // Outbox table update : SUCCESS
                outbox.updateStatus(Outbox.Status.SUCCESS);
                log.info("Message processed successfully");
            }
        } catch (Exception e) {
            log.error("Failed to process message: {}", e.getMessage());
        }
    }
}
