package io.hhplus.server.controller.user.dto.request;

import jakarta.validation.constraints.Positive;

public record UseRequest(
        @Positive int amount
) {
}
