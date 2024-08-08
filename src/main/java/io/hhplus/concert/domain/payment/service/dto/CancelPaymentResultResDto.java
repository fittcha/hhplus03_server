package io.hhplus.concert.domain.payment.service.dto;

import io.hhplus.concert.domain.payment.entity.Payment;

public record CancelPaymentResultResDto(
        boolean isSuccess,
        Long paymentId,
        Payment.Status status
) {
}
