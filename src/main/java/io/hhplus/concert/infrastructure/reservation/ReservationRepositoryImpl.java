package io.hhplus.concert.infrastructure.reservation;

import io.hhplus.concert.domain.reservation.entity.Reservation;
import io.hhplus.concert.domain.reservation.repository.ReservationJpaRepository;
import io.hhplus.concert.domain.reservation.repository.ReservationRepository;
import io.hhplus.concert.domain.reservation.service.dto.GetReservationAndPaymentResDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReservationRepositoryImpl implements ReservationRepository {

    private final ReservationJpaRepository reservationJpaRepository;

    public ReservationRepositoryImpl(ReservationJpaRepository reservationJpaRepository) {
        this.reservationJpaRepository = reservationJpaRepository;
    }

    @Override
    public Reservation findOneByConcertDateIdAndSeatNum(Long concertDateId, int seatNum) {
        return reservationJpaRepository.findOneByConcertDateIdAndSeatNum(concertDateId, seatNum);
    }

    @Override
    public Reservation save(Reservation reservation) {
        return reservationJpaRepository.save(reservation);
    }

    @Override
    public Reservation findByIdAndUserId(Long reservationId, Long userId) {
        return reservationJpaRepository.findByReservationIdAndUserId(reservationId, userId);
    }

    @Override
    public Reservation findById(Long reservationId) {
        return reservationJpaRepository.findById(reservationId).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public List<GetReservationAndPaymentResDto> getMyReservations(Long userId) {
        return reservationJpaRepository.getMyReservations(userId);
    }
}
