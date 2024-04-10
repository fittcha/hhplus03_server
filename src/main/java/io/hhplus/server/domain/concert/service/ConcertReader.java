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
    /* 타 도메인에서 Concert 관련 정보 단순 조회용 */

    private final ConcertRepository concertRepository;
    private final PlaceRepository placeRepository;

    public Concert getConcert(Long concertId) {
        return concertRepository.findById(concertId);
    }

    public ConcertDate getConcertDate(Long concertDateId) {
        return concertRepository.findConcertDateById(concertDateId);
    }

    public Place getPlace(Long placeId) {
        return placeRepository.findById(placeId);
    }

    public Seat getSeat(Long seatId) {
        return placeRepository.findSeatById(seatId);
    }
}
