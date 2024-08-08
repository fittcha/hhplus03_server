package io.hhplus.concert.domain.reservation.service.dto;

public record OccupyTempReservationDto(
        Long reservationId,
        long occupyTime
) {

    public static OccupyTempReservationDto toOccupy(Long reservationId) {
        return new OccupyTempReservationDto(reservationId, System.currentTimeMillis());
    }
}
