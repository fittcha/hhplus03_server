package io.hhplus.server.controller.waiting.dto.request;

import jakarta.validation.constraints.NotNull;

public record CheckActiveRequest(
        @NotNull Long userId,
        @NotNull String token
) {
}
