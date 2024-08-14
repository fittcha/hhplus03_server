package io.hhplus.server.domain.reservation.event;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
@ToString
public class ReserveEvent extends ApplicationEvent {

    private Long outboxId;
    private final Long reservationId;

    public ReserveEvent(Object source, Long outboxId, Long reservationId) {
        super(source);
        this.outboxId = outboxId;
        this.reservationId = reservationId;
    }
}
