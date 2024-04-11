package io.hhplus.server.domain.payment.service.dto;

import io.hhplus.server.domain.payment.entity.Payment;

public record CancelPaymentResultResDto(
        boolean isSuccess,
        Long paymentId,
        Payment.Status status
) {
}
