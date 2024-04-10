package io.hhplus.server.domain.concert.service;

import io.hhplus.server.domain.concert.entity.Seat;
import io.hhplus.server.domain.reservation.ReservationEnums;
import io.hhplus.server.domain.reservation.entity.Reservation;
import io.hhplus.server.domain.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ConcertReservationManager {

    private final ReservationRepository reservationRepository;

    public List<Long> getReservedSeatIdsByConcertDate(Long concertDateId) {
        // 예약 정보 조회
        List<Reservation> reservations = reservationRepository.findAllByConcertDateId(concertDateId);
        // 예약완료, 예약중인 좌석 PK 반환
        return reservations.stream()
                .filter(v -> List.of(ReservationEnums.Status.RESERVED, ReservationEnums.Status.ING).contains(v.getStatus()))
                .map(Reservation::getSeat)
                .map(Seat::getSeatId)
                .toList();
    }
}
