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

@Slf4j
@Service
@RequiredArgsConstructor
public class SendService {

    private final SendRepository sendRepository;

    public Send save(Send send) {
        return sendRepository.save(send);
    }

    public Send findById(Long sendId) {
        Send send = sendRepository.findById(sendId);
        if (send == null) {
            throw new RuntimeException("Not found send. sendId: " + sendId);
        }
        return send;
    }

    public void updateStatus(Send send, Send.Status status) {
        send.updateStatus(status);
    }

    @Transactional
    @Retryable(value = RuntimeException.class, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public boolean send(SendCommReqDto sendReqDto) {
        // dummy result :: 외부 데이터 플랫폼에 정보 전송 로직()
        return true;
    }

    @Recover
    public void sendRecover(RuntimeException e, Long reservationId) {
        log.error("All the retries failed. sendId: {}, error: {}", reservationId, e.getMessage());
    }
}
