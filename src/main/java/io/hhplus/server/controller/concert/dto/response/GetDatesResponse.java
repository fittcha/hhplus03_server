package io.hhplus.server.controller.concert.dto.response;

import io.hhplus.server.domain.concert.entity.ConcertDate;

import java.time.ZonedDateTime;

public record GetDatesResponse(
        Long concertDateId,
        ZonedDateTime date
) {

    public static GetDatesResponse from(ConcertDate concertDate) {
        return new GetDatesResponse(
                concertDate.getConcertDateId(),
                concertDate.getConcertDate()
        );
    }
}
