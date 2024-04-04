package io.hhplus.server.controller.concert.dto.response;

public record GetSeatsResponse(
        Long seatId,
        int seatNum,
        boolean isReserved
) {
}
