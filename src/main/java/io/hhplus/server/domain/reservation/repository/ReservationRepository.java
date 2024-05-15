package io.hhplus.server.domain.reservation.repository;

import io.hhplus.server.domain.reservation.entity.Reservation;
import io.hhplus.server.domain.reservation.service.dto.GetReservationAndPaymentResDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository {

    Reservation findOneByConcertDateIdAndSeatNum(Long concertDateId, int seatNum);

    Reservation save(Reservation reservation);

    Reservation findByIdAndUserId(Long reservationId, Long userId);

    Reservation findById(Long reservationId);

    List<GetReservationAndPaymentResDto> getMyReservations(Long userId);
}
