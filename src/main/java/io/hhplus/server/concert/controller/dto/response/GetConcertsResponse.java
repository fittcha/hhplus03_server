package io.hhplus.server.concert.controller.dto.response;

import java.time.ZonedDateTime;

public record GetConcertsResponse(
        Long concertId,
        String name,
        ZonedDateTime createdAt
) {
}
