package io.hhplus.server.domain.concert.service;

import io.hhplus.server.domain.concert.entity.Seat;
import io.hhplus.server.domain.reservation.entity.Reservation;
import io.hhplus.server.domain.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ConcertReservationManager {

    private final ReservationRepository reservationRepository;

    /* 콘서트 회차별 예약된 좌석 PK 목록 조회 */
    public List<Long> getReservedSeatIdsByConcertDate(Long concertDateId) {
        List<Reservation> reservations = reservationRepository.findAllByConcertDateId(concertDateId);
        // [예약 완료, 예약중]인 좌석 PK 반환
        return reservations.stream()
                .filter(v -> List.of(Reservation.Status.RESERVED, Reservation.Status.ING).contains(v.getStatus()))
                .map(Reservation::getSeat)
                .map(Seat::getSeatId)
                .toList();
    }
}
