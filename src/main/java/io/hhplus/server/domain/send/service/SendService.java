package io.hhplus.server.domain.send.service;

import io.hhplus.server.domain.send.event.SendEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendService {

    @Transactional
    public boolean send(SendEvent sendEvent) {
        // dummy result :: 외부 데이터 플랫폼에 정보 전송 로직()
        return true;
    }
}
