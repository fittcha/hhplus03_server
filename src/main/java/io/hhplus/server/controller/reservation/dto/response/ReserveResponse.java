package io.hhplus.server.controller.reservation.dto.response;

import io.hhplus.server.domain.concert.entity.Concert;
import io.hhplus.server.domain.concert.entity.ConcertDate;
import io.hhplus.server.domain.concert.entity.Seat;
import io.hhplus.server.domain.payment.PaymentEnums;
import io.hhplus.server.domain.payment.entity.Payment;
import io.hhplus.server.domain.reservation.ReservationEnums;
import io.hhplus.server.domain.reservation.entity.Reservation;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public record ReserveResponse(
        Long reservationId,
        ReservationEnums.Status status,
        ConcertInfo concertInfo,
        PaymentInfo paymentInfo
) {

    @Builder
    public ReserveResponse {
    }

    public static ReserveResponse from(Reservation reservation, Payment payment) {
        Concert concertInfo = reservation.getConcert();
        ConcertDate concertDateInfo = reservation.getConcertDate();
        Seat seatInfo = reservation.getSeat();

        return ReserveResponse.builder()
                .reservationId(reservation.getReservationId())
                .status(reservation.getStatus())
                .concertInfo(ConcertInfo.builder()
                        .concertId(concertInfo.getConcertId())
                        .concertDateId(concertDateInfo.getConcertDateId())
                        .name(concertInfo.getName())
                        .date(concertDateInfo.getConcertDate())
                        .seatId(seatInfo.getSeatId())
                        .seatNum(seatInfo.getSeatNum())
                        .build())
                .paymentInfo(PaymentInfo.builder()
                        .paymentId(payment.getPaymentId())
                        .status(payment.getStatus())
                        .paymentPrice(payment.getPrice())
                        .build())
                .build();
    }

    public record ConcertInfo(
            Long concertId,
            Long concertDateId,
            String name,
            ZonedDateTime date,
            Long seatId,
            int seatNum
    ) {
        @Builder
        public ConcertInfo {
        }
    }

    public record PaymentInfo(
            Long paymentId,
            PaymentEnums.Status status,
            BigDecimal paymentPrice
    ) {

        @Builder
        public PaymentInfo {
        }
    }

}
