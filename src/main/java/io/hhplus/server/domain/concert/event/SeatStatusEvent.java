package io.hhplus.server.domain.concert.event;

import io.hhplus.server.domain.concert.entity.Seat;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class SeatStatusEvent extends ApplicationEvent {

    private final Long concertDateId;
    private final int seatNum;
    private final Seat.Status status;

    public SeatStatusEvent(Object source, Long concertDateId, int seatNum, Seat.Status status) {
        super(source);
        this.concertDateId = concertDateId;
        this.seatNum = seatNum;
        this.status = status;
    }
}
