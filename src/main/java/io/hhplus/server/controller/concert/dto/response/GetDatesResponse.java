package io.hhplus.server.controller.concert.dto.response;

import java.time.ZonedDateTime;
import java.util.List;

public record GetDatesResponse(
        List<DatesStatus> dates
) {

    public record DatesStatus(
            ZonedDateTime date,
            boolean isSoldOut
    ) {
    }
}
