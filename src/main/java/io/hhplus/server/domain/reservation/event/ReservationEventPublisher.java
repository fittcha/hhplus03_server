package io.hhplus.server.domain.reservation.event;

import io.hhplus.server.domain.concert.event.SeatStatusEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void reservationOccupy(ReservationOccupiedEvent event) {
        applicationEventPublisher.publishEvent(event);
    }

    public void reservationCancel(ReservationCancelledEvent event) {
        applicationEventPublisher.publishEvent(event);
    }

    public void updateSeatStatus(SeatStatusEvent event) {
        applicationEventPublisher.publishEvent(event);
    }

}
