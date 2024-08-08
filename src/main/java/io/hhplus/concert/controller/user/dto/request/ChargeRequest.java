package io.hhplus.concert.controller.user.dto.request;

import jakarta.validation.constraints.Positive;

public record ChargeRequest(
        @Positive int amount
) {
}
