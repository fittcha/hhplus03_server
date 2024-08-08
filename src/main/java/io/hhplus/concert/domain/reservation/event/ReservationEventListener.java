package io.hhplus.concert.domain.reservation.event;

import io.hhplus.concert.base.kafka.KafkaConstants;
import io.hhplus.concert.base.kafka.service.KafkaProducer;
import io.hhplus.concert.domain.outbox.entity.Outbox;
import io.hhplus.concert.domain.outbox.service.OutboxService;
import io.hhplus.concert.domain.reservation.service.ReservationMonitor;
import io.hhplus.concert.domain.reservation.service.ReservationReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ReservationEventListener {

    private final OutboxService outboxService;
    private final KafkaProducer kafkaProducer;
    private final ReservationMonitor reservationMonitor;
    private final ReservationReader reservationReader;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void occupyReservation(ReserveEvent event) {
        // 예약 임시 점유 모니터링
        reservationMonitor.occupyReservation(event.getReservationId());
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void saveOutboxReserve(ReserveEvent event) {
        // Outbox 데이터 등록
        Outbox outbox = outboxService.save(Outbox.toEntity(Outbox.Type.RESERVE, Outbox.Status.INIT, String.valueOf(event.getReservationId())));
        // set outboxId
        event.setOutboxId(outbox.getOutboxId());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onReservedEvent(ReserveEvent event) {
        // 예약 완료 kafka 발행
        kafkaProducer.publish(KafkaConstants.RESERVATION_TOPIC, event.getOutboxId(), String.valueOf(event.getReservationId()));
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void saveOutboxCancel(CancelEvent event) {
        // Outbox 데이터 등록
        Outbox outbox = outboxService.save(Outbox.toEntity(Outbox.Type.CANCEL, Outbox.Status.INIT, String.valueOf(event.getReservationId())));
        // set outboxId
        event.setOutboxId(outbox.getOutboxId());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onCancelEvent(CancelEvent event) {
        // 예약 완료 kafka 발행
        kafkaProducer.publish(KafkaConstants.CANCEL_TOPIC, event.getOutboxId(), String.valueOf(event.getReservationId()));
    }
}
