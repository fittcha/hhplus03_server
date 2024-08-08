package io.hhplus.concert.domain.send.service;

import io.hhplus.concert.domain.outbox.entity.Outbox;
import io.hhplus.concert.domain.outbox.repository.OutboxRepository;
import io.hhplus.concert.domain.send.dto.SendCommReqDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendService {

    private final OutboxRepository outboxRepository;

    public Outbox save(Outbox outbox) {
        return outboxRepository.save(outbox);
    }

    @Transactional
    @Retryable(value = RuntimeException.class, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public void send(SendCommReqDto sendReqDto) {
        // dummy result :: 외부 데이터 플랫폼에 정보 전송 로직()
        // externalApiService.send(sendReqDto);
    }

    @Recover
    public void recoverSend(RuntimeException e, Long reservationId) {
        log.error("All the sendEvent retries failed. sendId: {}, error: {}", reservationId, e.getMessage());
    }
}
