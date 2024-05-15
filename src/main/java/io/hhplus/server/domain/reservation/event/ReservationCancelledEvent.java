package io.hhplus.server.domain.reservation.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ReservationCancelledEvent extends ApplicationEvent {

    private final Long reservationId;

    public ReservationCancelledEvent(Object source, Long reservationId) {
        super(source);
        this.reservationId = reservationId;
    }
}
