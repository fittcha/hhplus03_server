package io.hhplus.server.domain.payment.service.dto;

import io.hhplus.server.domain.payment.PaymentEnums;

public record CancelPaymentResultResDto(
        boolean isSuccess,
        Long paymentId,
        PaymentEnums.Status status
) {
}
