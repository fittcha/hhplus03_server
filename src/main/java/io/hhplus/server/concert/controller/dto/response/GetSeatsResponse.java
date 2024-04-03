package io.hhplus.server.concert.controller.dto.response;

import java.util.List;

public record GetSeatsResponse(
        List<SeatsStatus> seats
) {

    public record SeatsStatus(
            int seatNum,
            boolean available
    ) {
    }
}
