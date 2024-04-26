package io.hhplus.server.domain.concert.service;

import io.hhplus.server.domain.concert.entity.Concert;
import io.hhplus.server.domain.concert.entity.ConcertDate;
import io.hhplus.server.domain.concert.entity.Place;
import io.hhplus.server.domain.concert.entity.Seat;
import io.hhplus.server.domain.concert.repository.ConcertRepository;
import io.hhplus.server.domain.concert.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConcertReader {
    /* Concert 관련 정보 단순 조회용 */

    private final ConcertRepository concertRepository;

    public Concert findConcert(Long concertId) {
        return concertRepository.findById(concertId);
    }

    public ConcertDate findConcertDate(Long concertDateId) {
        return concertRepository.findConcertDateById(concertDateId);
    }

    public Seat findSeat(Long concertDateId, int seatNum) {
        return concertRepository.findSeatByConcertDateIdAndSeatNum(concertDateId, seatNum);
    }
}
