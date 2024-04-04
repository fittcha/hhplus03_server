package io.hhplus.server.controller.payment.dto.response;

import io.hhplus.server.domain.payment.PaymentEnums;
import lombok.Builder;

public record PayResponse(
        Long paymentId,
        PaymentEnums.Status status,
        int paymentPrice,
        int balance
) {
    @Builder
    public PayResponse {
    }
}
