package io.hhplus.concert.domain.reservation.service.dto;

import io.hhplus.concert.domain.concert.entity.Concert;
import io.hhplus.concert.domain.concert.entity.ConcertDate;
import io.hhplus.concert.domain.concert.entity.Seat;
import io.hhplus.concert.domain.reservation.entity.Reservation;

public record GetReservationAndPaymentResDto(
        Reservation reservation,
        Concert concert,
        ConcertDate concertDate,
        Seat seat
) {
}
