package io.hhplus.concert.controller.waiting.dto.request;

public record CheckWaitingRequest(
        Long userId,
        String token
) {
}
