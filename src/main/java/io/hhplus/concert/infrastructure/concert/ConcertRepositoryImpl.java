package io.hhplus.concert.infrastructure.concert;

import io.hhplus.concert.domain.concert.entity.Concert;
import io.hhplus.concert.domain.concert.entity.ConcertDate;
import io.hhplus.concert.domain.concert.entity.Seat;
import io.hhplus.concert.domain.concert.repository.ConcertDateJpaRepository;
import io.hhplus.concert.domain.concert.repository.ConcertJpaRepository;
import io.hhplus.concert.domain.concert.repository.ConcertRepository;
import io.hhplus.concert.domain.concert.repository.SeatJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ConcertRepositoryImpl implements ConcertRepository {

    private final ConcertJpaRepository concertJpaRepository;
    private final ConcertDateJpaRepository concertDateJpaRepository;
    private final SeatJpaRepository seatJpaRepository;

    public ConcertRepositoryImpl(ConcertJpaRepository concertJpaRepository, ConcertDateJpaRepository concertDateJpaRepository, ConcertDateJpaRepository concertDateJpaRepository1, SeatJpaRepository seatJpaRepository) {
        this.concertJpaRepository = concertJpaRepository;
        this.concertDateJpaRepository = concertDateJpaRepository1;
        this.seatJpaRepository = seatJpaRepository;
    }

    @Override
    public List<Concert> findAll() {
        return concertJpaRepository.findAll();
    }

    @Override
    public Concert findById(Long concertId) {
        return concertJpaRepository.findById(concertId).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public ConcertDate findConcertDateById(Long concertDateId) {
        return concertDateJpaRepository.findById(concertDateId).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public void addConcertDates(List<ConcertDate> concertDates) {
        concertDateJpaRepository.saveAll(concertDates);
    }

    @Override
    public void addConcert(Concert concert) {
        concertJpaRepository.save(concert);
    }

    @Override
    public void deleteAll() {
        concertDateJpaRepository.deleteAll();
        concertJpaRepository.deleteAll();
    }

    @Override
    public void deleteAllDates() {
        concertDateJpaRepository.deleteAll();
    }

    @Override
    public boolean existByConcertDateAndStatus(Long concertDateId, Seat.Status status) {
        return seatJpaRepository.existsByConcertDate_concertDateIdAndStatus(concertDateId, status);
    }

    @Override
    public List<Seat> findSeatsByConcertDateIdAndStatus(Long concertDateId, Seat.Status status) {
        return seatJpaRepository.findAllByConcertDate_concertDateIdAndStatus(concertDateId, status);
    }

    @Override
    public Seat findSeatByConcertDateIdAndSeatNum(Long concertDateId, int seatNum) {
        return seatJpaRepository.findSeatByConcertDate_concertDateIdAndSeatNum(concertDateId, seatNum);
    }

    @Override
    public void addSeats(List<Seat> seats) {
        seatJpaRepository.saveAll(seats);
    }
}
