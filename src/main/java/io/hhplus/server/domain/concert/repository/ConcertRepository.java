package io.hhplus.server.domain.concert.repository;

import io.hhplus.server.domain.concert.entity.Concert;
import io.hhplus.server.domain.concert.entity.ConcertDate;
import io.hhplus.server.domain.concert.entity.Seat;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConcertRepository {

    List<Concert> findAll();

    Concert findById(Long concertId);

    ConcertDate findConcertDateById(Long concertDateId);

    void addConcertDates(List<ConcertDate> concertDates);

    void addConcert(Concert concert);

    void deleteAll();

    void deleteAllDates();

    boolean existByConcertDateAndStatus(Long concertDateId, Seat.Status status);

    List<Seat> findSeatsByConcertDateIdAndStatus(Long concertDateId, Seat.Status status);

    Seat findSeatById(Long seatId);

    void addSeats(List<Seat> seats);
}
