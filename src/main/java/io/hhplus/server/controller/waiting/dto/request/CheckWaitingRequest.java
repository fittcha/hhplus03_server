package io.hhplus.server.controller.waiting.dto.request;

public record CheckWaitingRequest(
        Long userId,
        String token
) {
}
