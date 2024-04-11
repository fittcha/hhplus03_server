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
    private final PlaceRepository placeRepository;

    public Concert findConcert(Long concertId) {
        return concertRepository.findById(concertId);
    }

    public ConcertDate findConcertDate(Long concertDateId) {
        return concertRepository.findConcertDateById(concertDateId);
    }

    public Place findPlace(Long placeId) {
        return placeRepository.findById(placeId);
    }

    public Seat findSeat(Long seatId) {
        return placeRepository.findSeatById(seatId);
    }
}
