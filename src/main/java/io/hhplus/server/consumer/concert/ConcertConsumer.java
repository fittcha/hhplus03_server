package io.hhplus.server.consumer.concert;

import io.hhplus.server.base.kafka.KafkaConstants;
import io.hhplus.server.base.util.JsonUtil;
import io.hhplus.server.domain.concert.event.SeatStatusEvent;
import io.hhplus.server.domain.concert.service.ConcertService;
import io.hhplus.server.domain.outbox.entity.Outbox;
import io.hhplus.server.domain.outbox.service.OutboxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConcertConsumer {

    private final ConcertService concertService;
    private final OutboxService outboxService;

    @KafkaListener(topics = KafkaConstants.SEAT_STATUS_TOPIC, groupId = "hhplus-01")
    public void seatStatus(String outboxId, String jsonData) {
        try {
            log.info("Received SEAT_STATUS_TOPIC: {}", outboxId);

            Outbox outbox = outboxService.findById(Long.valueOf(outboxId));
            // Outbox update :: PUBLISHED
            outboxService.updateStatus(outbox, Outbox.Status.PUBLISHED);

            // update seat status
            SeatStatusEvent seatStatusEvent = JsonUtil.fromJson(jsonData, SeatStatusEvent.class);
            concertService.patchSeatStatus(seatStatusEvent.getConcertDateId(), seatStatusEvent.getSeatNum(), seatStatusEvent.getStatus());

            // Outbox table update : SUCCESS
            outbox.updateStatus(Outbox.Status.SUCCESS);
            log.info("Message processed successfully");

        } catch (Exception e) {
            log.error("Failed to process message: {}", e.getMessage());
        }
    }
}
