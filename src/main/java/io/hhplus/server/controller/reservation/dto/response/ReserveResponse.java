package io.hhplus.server.controller.reservation.dto.response;

import io.hhplus.server.domain.concert.entity.Concert;
import io.hhplus.server.domain.concert.entity.ConcertDate;
import io.hhplus.server.domain.reservation.entity.Reservation;
import lombok.Builder;

import java.time.ZonedDateTime;

public record ReserveResponse(
        Long reservationId,
        Reservation.Status status,
        ConcertInfo concertInfo
) {

    @Builder
    public ReserveResponse {
    }

    public static ReserveResponse from(Reservation reservation, Concert concert, ConcertDate concertDate) {
        return ReserveResponse.builder()
                .reservationId(reservation.getReservationId())
                .status(reservation.getStatus())
                .concertInfo(ConcertInfo.builder()
                        .concertId(concert.getConcertId())
                        .concertDateId(concertDate.getConcertDateId())
                        .name(concert.getName())
                        .date(concertDate.getConcertDate())
                        .seatNum(reservation.getSeatNum())
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

}
