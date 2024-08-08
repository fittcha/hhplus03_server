package io.hhplus.concert.controller.payment.dto.request;

import jakarta.validation.constraints.NotNull;

public record PayRequest(
        @NotNull Long userId
) {
}
