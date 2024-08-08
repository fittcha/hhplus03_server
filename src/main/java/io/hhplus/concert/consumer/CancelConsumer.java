package io.hhplus.concert.consumer;

import io.hhplus.concert.base.kafka.KafkaConstants;
import io.hhplus.concert.base.util.JsonUtil;
import io.hhplus.concert.domain.outbox.service.OutboxService;
import io.hhplus.concert.domain.reservation.entity.Reservation;
import io.hhplus.concert.domain.reservation.service.ReservationReader;
import io.hhplus.concert.domain.send.dto.SendCommReqDto;
import io.hhplus.concert.domain.send.service.SendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CancelConsumer {

    private final SendService sendService;
    private final OutboxService outboxService;
    private final ReservationReader reservationReader;

    @KafkaListener(topics = KafkaConstants.CANCEL_TOPIC, groupId = "hhplus-01")
    public void reserved(String outboxId, String message) {
        try {
            log.info("Received CANCEL_TOPIC: {}", outboxId);
            outboxService.toPublished(Long.valueOf(outboxId));

            Reservation reservation = reservationReader.findReservation(Long.valueOf(message));

            // 예약 취소 정보 전송
            sendService.send(new SendCommReqDto(SendCommReqDto.DataType.CANCEL, JsonUtil.toJson(reservation)));

            log.info("CANCEL_TOPIC: Message processed successfully");
        } catch (Exception e) {
            log.error("Failed to process message: {}", e.getMessage());
        }
    }
}
