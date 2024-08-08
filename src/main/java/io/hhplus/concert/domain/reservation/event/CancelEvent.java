package io.hhplus.concert.domain.reservation.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class CancelEvent extends ApplicationEvent {

    private Long outboxId;
    private final Long reservationId;

    public CancelEvent(Object source, Long outboxId, Long reservationId) {
        super(source);
        this.outboxId = outboxId;
        this.reservationId = reservationId;
    }
}
