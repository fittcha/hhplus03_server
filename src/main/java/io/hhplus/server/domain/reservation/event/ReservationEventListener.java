package io.hhplus.server.domain.reservation.event;

import io.hhplus.server.base.util.JsonUtil;
import io.hhplus.server.domain.concert.entity.Seat;
import io.hhplus.server.domain.concert.service.ConcertService;
import io.hhplus.server.domain.outbox.service.OutboxService;
import io.hhplus.server.domain.payment.service.PaymentService;
import io.hhplus.server.domain.reservation.entity.Reservation;
import io.hhplus.server.domain.reservation.service.ReservationMonitor;
import io.hhplus.server.domain.send.dto.SendCommReqDto;
import io.hhplus.server.domain.send.service.SendService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ReservationEventListener {

    private final OutboxService outboxService;
    private final ReservationMonitor reservationMonitor;
    private final SendService sendService;
    private final PaymentService paymentService;
    private final ConcertService concertService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onReservedEvent(ReserveEvent event) {
        // 발행 완료
        outboxService.toPublished(event.getOutboxId());

        // 예약 임시 점유 모니터링
        reservationMonitor.occupyReservation(event.getReservation().getReservationId());

        // 예약 정보 전송
        String jsonData = JsonUtil.toJson(event.getReservation());
        sendService.send(new SendCommReqDto(SendCommReqDto.DataType.RESERVATION, jsonData));
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onCancelEvent(CancelEvent event) {
        // 발행 완료
        outboxService.toPublished(event.getOutboxId());

        Reservation reservation = event.getReservation();
        // 결제 내역 환불 처리
        paymentService.refundReservationCancelled(reservation.getReservationId());
        // 좌석 상태 변경
        concertService.patchSeatStatus(reservation.getConcertDateId(), reservation.getSeatNum(), Seat.Status.AVAILABLE);
        // 예약 정보 전송
        String jsonData = JsonUtil.toJson(event.getReservation());
        sendService.send(new SendCommReqDto(SendCommReqDto.DataType.RESERVATION, jsonData));
    }
}

