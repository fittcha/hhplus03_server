package io.hhplus.server.domain.concert.repository;

import io.hhplus.server.domain.concert.entity.Seat;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SeatJpaRepository extends JpaRepository<Seat, Long> {

    boolean existsByConcertDate_concertDateIdAndStatus(Long concertDateId, Seat.Status status);

    List<Seat> findAllByConcertDate_concertDateIdAndStatus(Long concertDateId, Seat.Status status);

    Seat findSeatByConcertDate_concertDateIdAndSeatNum(@Param("concertDateId") Long concertDateId, @Param("seatNum") int seatNum);
}
