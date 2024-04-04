package io.hhplus.server.controller.reservation.dto.request;

import java.time.ZonedDateTime;

public record ReserveRequest(
        Long concertId,
        ZonedDateTime date,
        int seatNum,
        Long userId
) {
}
