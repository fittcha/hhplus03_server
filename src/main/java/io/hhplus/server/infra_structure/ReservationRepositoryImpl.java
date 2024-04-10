package io.hhplus.server.infra_structure;

import io.hhplus.server.domain.reservation.entity.Reservation;
import io.hhplus.server.domain.reservation.repository.ReservationJpaRepository;
import io.hhplus.server.domain.reservation.repository.ReservationRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReservationRepositoryImpl implements ReservationRepository {

    private final ReservationJpaRepository reservationJpaRepository;

    public ReservationRepositoryImpl(ReservationJpaRepository reservationJpaRepository) {
        this.reservationJpaRepository = reservationJpaRepository;
    }

    @Override
    public List<Reservation> findAllByConcertDateId(Long concertDateId) {
        return reservationJpaRepository.findAllByConcertDate_ConcertDateId(concertDateId);
    }
}
