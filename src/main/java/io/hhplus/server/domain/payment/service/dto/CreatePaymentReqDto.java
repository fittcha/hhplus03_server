package io.hhplus.server.domain.payment.service.dto;

import io.hhplus.server.domain.payment.PaymentEnums;
import io.hhplus.server.domain.payment.entity.Payment;
import io.hhplus.server.domain.reservation.entity.Reservation;

import java.math.BigDecimal;

public record CreatePaymentReqDto(
        Reservation reservation,
        PaymentEnums.Status status,
        BigDecimal price
) {

    public Payment toEntity() {
        return new Payment(reservation, status, price);
    }
}
