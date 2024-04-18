package io.hhplus.server.domain.reservation.service;

import io.hhplus.server.domain.reservation.entity.Reservation;
import io.hhplus.server.domain.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationReader {
    /* Reservation 관련 정보 단순 조회용 */

    private final ReservationRepository reservationRepository;

    public Reservation findReservation(Long reservationId) {
        return reservationRepository.findById(reservationId);
    }
}
