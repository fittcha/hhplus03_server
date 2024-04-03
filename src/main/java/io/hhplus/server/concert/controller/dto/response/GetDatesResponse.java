package io.hhplus.server.concert.controller.dto.response;

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
