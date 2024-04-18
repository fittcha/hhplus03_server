package io.hhplus.server.domain.reservation.event;

import io.hhplus.server.domain.reservation.service.ReservationMonitor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ReservationEventListener {

    private final ReservationMonitor reservationMonitor;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onReservationOccupiedEvent(ReservationOccupiedEvent event) {
        reservationMonitor.occupyReservation(event.getReservationId());
    }
}

