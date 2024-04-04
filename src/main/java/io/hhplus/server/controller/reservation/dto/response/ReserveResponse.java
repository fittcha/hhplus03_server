package io.hhplus.server.controller.reservation.dto.response;

import java.time.ZonedDateTime;

public record ReserveResponse(
        Long reservationId,
        String status,
        ConcertInfo concertInfo,
        PaymentInfo paymentInfo
) {

    public record ConcertInfo(
            Long concertId,
            String name,
            ZonedDateTime date,
            int seatNum
    ) {
    }

    public record PaymentInfo(
            Long paymentId,
            String status,
            int paymentPrice
    ) {
    }

}
