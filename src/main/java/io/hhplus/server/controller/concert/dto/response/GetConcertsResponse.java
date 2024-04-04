package io.hhplus.server.controller.concert.dto.response;

import java.time.ZonedDateTime;

public record GetConcertsResponse(
        Long concertId,
        String name,
        ZonedDateTime createdAt
) {
}
