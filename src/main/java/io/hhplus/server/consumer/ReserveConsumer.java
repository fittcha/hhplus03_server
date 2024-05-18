package io.hhplus.server.consumer;

import io.hhplus.server.base.kafka.KafkaConstants;
import io.hhplus.server.base.util.JsonUtil;
import io.hhplus.server.domain.reservation.entity.Reservation;
import io.hhplus.server.domain.reservation.service.ReservationMonitor;
import io.hhplus.server.domain.send.dto.SendCommReqDto;
import io.hhplus.server.domain.send.service.SendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReserveConsumer {

    private final ReservationMonitor reservationMonitor;
    private final SendService sendService;

    @KafkaListener(topics = KafkaConstants.RESERVATION_TOPIC, groupId = "hhplus-01")
    public void reserved(String outboxId, String message) {
        try {
            log.info("Received RESERVATION_TOPIC: {}", outboxId);

            Reservation reservation = JsonUtil.fromJson(message, Reservation.class);

            // 예약 임시 점유 모니터링
            reservationMonitor.occupyReservation(reservation.getReservationId());

            // 예약 정보 전송
            sendService.send(new SendCommReqDto(SendCommReqDto.DataType.RESERVATION, message));

            log.info("RESERVATION_TOPIC: Message processed successfully");
        } catch (Exception e) {
            log.error("Failed to process message: {}", e.getMessage());
        }
    }
}
