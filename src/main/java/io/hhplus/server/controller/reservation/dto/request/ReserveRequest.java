package io.hhplus.server.controller.reservation.dto.request;

import io.hhplus.server.domain.concert.entity.Concert;
import io.hhplus.server.domain.concert.entity.ConcertDate;
import io.hhplus.server.domain.concert.entity.Seat;
import io.hhplus.server.domain.concert.service.ConcertReader;
import io.hhplus.server.domain.payment.entity.Payment;
import io.hhplus.server.domain.payment.service.dto.CreatePaymentReqDto;
import io.hhplus.server.domain.reservation.entity.Reservation;
import io.hhplus.server.domain.user.entity.User;
import io.hhplus.server.domain.user.service.UserReader;
import jakarta.validation.constraints.NotNull;

public record ReserveRequest(
        @NotNull Long concertId,
        @NotNull Long concertDateId,
        @NotNull Long seatId,
        @NotNull Long userId
) {

    public Reservation toEntity(ConcertReader concertReader, UserReader userReader) {
        Concert concert = concertReader.findConcert(concertId);
        ConcertDate concertDate = concertReader.findConcertDate(concertDateId);
        Seat seat = concertReader.findSeat(seatId);
        User user = userReader.findUser(userId);

        return Reservation.builder()
                .concert(concert)
                .concertDate(concertDate)
                .seat(seat)
                .user(user)
                .status(Reservation.Status.ING)
                .build();
    }

    public CreatePaymentReqDto toCreatePayment(Reservation reservation) {
        return new CreatePaymentReqDto(reservation, Payment.Status.READY, reservation.getSeat().getPrice());
    }
}
