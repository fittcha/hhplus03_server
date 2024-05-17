package io.hhplus.server.domain.concert.event;

import io.hhplus.server.domain.concert.service.ConcertService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ConcertEventListener {

    private final ConcertService concertService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onSeatStatusEvent(SeatStatusEvent event) {
        concertService.patchSeatStatus(event.getConcertDateId(), event.getSeatNum(), event.getStatus());
    }
}

