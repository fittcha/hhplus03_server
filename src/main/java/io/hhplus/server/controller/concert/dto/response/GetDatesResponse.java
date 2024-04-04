package io.hhplus.server.controller.concert.dto.response;

import java.time.ZonedDateTime;

public record GetDatesResponse(
        Long concertDateId,
        ZonedDateTime date,
        boolean isSoldOut
) {
}
