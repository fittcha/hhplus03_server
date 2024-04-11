package io.hhplus.server.domain.reservation.service.dto;

import io.hhplus.server.domain.payment.entity.Payment;
import io.hhplus.server.domain.reservation.entity.Reservation;

public record GetReservationAndPaymentResDto(
        Reservation reservation,
        Payment payment
) {
}
