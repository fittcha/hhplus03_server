package io.hhplus.server.domain.send.service;

import io.hhplus.server.domain.send.dto.SendCommReqDto;
import io.hhplus.server.domain.send.entity.Send;
import io.hhplus.server.domain.send.repository.SendRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static io.hhplus.server.domain.send.entity.Send.Status.DONE;
import static io.hhplus.server.domain.send.entity.Send.Status.ING;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendService {

    private final SendRepository sendRepository;

    public Send save(Send send) {
        return sendRepository.save(send);
    }

    @Transactional
    @Retryable(value = RuntimeException.class, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public void send(SendCommReqDto sendReqDto) {
        Send send = sendRepository.findById(sendReqDto.getSendId());
        if (send == null) {
            throw new RuntimeException("Not found send. sendId: " + sendReqDto.getSendId());
        }
        // Outbox table update : {ING}
        send.updateStatus(ING);

        // dummy result :: 외부 데이터 플랫폼에 정보 전송 로직()
        boolean isSuccess = true;
        if (isSuccess) {
            // Outbox table update : {DONE}
            send.updateStatus(DONE);
        }
    }

    @Recover
    public void sendRecover(RuntimeException e, Long reservationId) {
        log.error("All the retries failed. sendId: {}, error: {}", reservationId, e.getMessage());
    }
}
