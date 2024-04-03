package io.hhplus.server.concert.controller.dto.response;

import java.time.ZonedDateTime;

public record GetConcertResponse(
        Long concertId,
        String name,
        String place,
        int ticketPrice,
        ZonedDateTime createdAt
) {
}
