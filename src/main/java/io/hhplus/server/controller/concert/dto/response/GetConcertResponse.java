package io.hhplus.server.controller.concert.dto.response;

import java.time.ZonedDateTime;

public record GetConcertResponse(
        Long concertId,
        String name,
        String place,
        int ticketPrice,
        ZonedDateTime createdAt
) {
}
