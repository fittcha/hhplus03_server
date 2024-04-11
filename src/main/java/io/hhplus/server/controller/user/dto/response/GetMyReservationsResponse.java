package io.hhplus.server.controller.user.dto.response;

import io.hhplus.server.domain.concert.entity.Concert;
import io.hhplus.server.domain.concert.entity.ConcertDate;
import io.hhplus.server.domain.concert.entity.Seat;
import io.hhplus.server.domain.payment.PaymentEnums;
import io.hhplus.server.domain.payment.entity.Payment;
import io.hhplus.server.domain.reservation.ReservationEnums;
import io.hhplus.server.domain.reservation.entity.Reservation;
import io.hhplus.server.domain.reservation.service.dto.GetReservationAndPaymentResDto;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public record GetMyReservationsResponse(

        Long reservationId,
        ReservationEnums.Status status,
        ConcertInfo concertInfo,
        PaymentInfo paymentInfo
) {
    @Builder
    public GetMyReservationsResponse {
    }

    public static GetMyReservationsResponse from(GetReservationAndPaymentResDto resDto) {
        if (resDto == null || resDto.reservation() == null) {
            return null;
        }

        Reservation reservation = resDto.reservation();
        Concert concertInfo = reservation.getConcert();
        ConcertDate concertDateInfo = reservation.getConcertDate();
        Seat seatInfo = reservation.getSeat();
        Payment payment = resDto.payment();

        return GetMyReservationsResponse.builder()
                .reservationId(reservation.getReservationId())
                .status(reservation.getStatus())
                .concertInfo(GetMyReservationsResponse.ConcertInfo.builder()
                        .concertId(concertInfo.getConcertId())
                        .concertDateId(concertDateInfo.getConcertDateId())
                        .name(concertInfo.getName())
                        .date(concertDateInfo.getConcertDate())
                        .seatId(seatInfo.getSeatId())
                        .seatNum(seatInfo.getSeatNum())
                        .build())
                .paymentInfo(GetMyReservationsResponse.PaymentInfo.builder()
                        .paymentId(payment.getPaymentId())
                        .status(payment.getStatus())
                        .paymentPrice(payment.getPrice())
                        .build())
                .build();
    }

    @Builder
    public static record ConcertInfo(
            Long concertId,
            Long concertDateId,
            String name,
            ZonedDateTime date,
            Long seatId,
            int seatNum
    ) {
    }

    @Builder
    public static record PaymentInfo(
            Long paymentId,
            PaymentEnums.Status status,
            BigDecimal paymentPrice
    ) {
    }

}
