package io.hhplus.server.domain.concert.event;

import io.hhplus.server.domain.concert.entity.Seat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SeatStatusEvent {

    private final Long concertDateId;
    private final int seatNum;
    private final Seat.Status status;
}
