package io.hhplus.concert.domain.concert.repository;

import io.hhplus.concert.domain.concert.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SeatJpaRepository extends JpaRepository<Seat, Long> {

    boolean existsByConcertDate_concertDateIdAndStatus(Long concertDateId, Seat.Status status);

    List<Seat> findAllByConcertDate_concertDateIdAndStatus(Long concertDateId, Seat.Status status);

    Seat findSeatByConcertDate_concertDateIdAndSeatNum(@Param("concertDateId") Long concertDateId, @Param("seatNum") int seatNum);
}
