package io.hhplus.server.consumer;

import io.hhplus.server.base.kafka.KafkaConstants;
import io.hhplus.server.base.util.JsonUtil;
import io.hhplus.server.domain.concert.entity.Seat;
import io.hhplus.server.domain.concert.service.ConcertService;
import io.hhplus.server.domain.payment.service.PaymentService;
import io.hhplus.server.domain.reservation.entity.Reservation;
import io.hhplus.server.domain.send.dto.SendCommReqDto;
import io.hhplus.server.domain.send.service.SendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CancelConsumer {

    private final PaymentService paymentService;
    private final ConcertService concertService;
    private final SendService sendService;

    @KafkaListener(topics = KafkaConstants.CANCEL_TOPIC, groupId = "hhplus-01")
    public void reserved(String outboxId, String message) {
        try {
            log.info("Received CANCEL_TOPIC: {}", outboxId);

            Reservation reservation = JsonUtil.fromJson(message, Reservation.class);

            // 결제 내역 환불 처리
            paymentService.refundReservationCancelled(reservation.getReservationId());
            // 좌석 상태 변경
            concertService.patchSeatStatus(reservation.getConcertDateId(), reservation.getSeatNum(), Seat.Status.AVAILABLE);
            // 예약 취소 정보 전송
            sendService.send(new SendCommReqDto(SendCommReqDto.DataType.CANCEL, message));

            log.info("CANCEL_TOPIC: Message processed successfully");
        } catch (Exception e) {
            log.error("Failed to process message: {}", e.getMessage());
        }
    }
}
