package io.hhplus.server.base.kafka.service;

import io.hhplus.server.base.kafka.KafkaConstants;
import io.hhplus.server.domain.send.dto.SendCommReqDto;
import io.hhplus.server.domain.send.entity.Send;
import io.hhplus.server.domain.send.service.SendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumer {

    private final SendService sendService;

    @KafkaListener(topics = KafkaConstants.RESERVATION_TOPIC, groupId = "hhplus-reservation")
    public void listen(String sendId, String message) {
        try {
            log.info("Received Message: {}", message);

            Send send = sendService.findById(Long.valueOf(sendId));
            // Outbox update :: ING
            sendService.updateStatus(send, Send.Status.ING);

            // send to DataPlatform
            SendCommReqDto sendCommReqDto = new SendCommReqDto(Long.valueOf(sendId), message);
            sendService.send(sendCommReqDto);

            log.info("Message processed successfully");
        } catch (Exception e) {
            log.error("Failed to process message: {}", e.getMessage());
        }
    }
}
