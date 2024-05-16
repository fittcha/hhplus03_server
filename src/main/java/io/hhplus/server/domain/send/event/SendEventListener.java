package io.hhplus.server.domain.send.event;

import io.hhplus.server.domain.send.service.SendService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class SendEventListener {

    private final SendService sendService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onSendEvent(SendEvent event) {
        sendService.send(event.getSendReqDto());
    }
}
