package io.hhplus.server.controller.payment.dto.request;

import jakarta.validation.constraints.NotNull;

public record PayRequest(
        @NotNull Long userId
) {
}
