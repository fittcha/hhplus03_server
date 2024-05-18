package io.hhplus.server.domain.reservation.event;

import io.hhplus.server.domain.reservation.entity.Reservation;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ReserveEvent extends ApplicationEvent {

    private final Long outboxId;
    private final Reservation reservation;

    public ReserveEvent(Object source, Long outboxId, Reservation reservation) {
        super(source);
        this.outboxId = outboxId;
        this.reservation = reservation;
    }
}
