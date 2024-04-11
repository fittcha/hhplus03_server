package io.hhplus.server.controller.waiting.dto.request;

import jakarta.validation.constraints.NotNull;

public record AddWaitingQueueRequest(
        @NotNull Long userId,
        @NotNull String token
) {
}
