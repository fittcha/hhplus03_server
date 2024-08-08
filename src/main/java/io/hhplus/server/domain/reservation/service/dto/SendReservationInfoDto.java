package io.hhplus.server.domain.reservation.service.dto;

import io.hhplus.server.domain.reservation.entity.Reservation;

public record SendReservationInfoDto(
        Long reservationId,
        Reservation.Status reservationStatus
) {
}
