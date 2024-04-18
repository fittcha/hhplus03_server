package io.hhplus.server.domain.reservation.service.dto;

import io.hhplus.server.domain.concert.entity.Concert;
import io.hhplus.server.domain.concert.entity.ConcertDate;
import io.hhplus.server.domain.concert.entity.Seat;
import io.hhplus.server.domain.reservation.entity.Reservation;

public record GetReservationAndPaymentResDto(
        Reservation reservation,
        Concert concert,
        ConcertDate concertDate,
        Seat seat
) {
}
