package io.hhplus.server.domain.concert.service;

import io.hhplus.server.domain.concert.entity.Concert;
import io.hhplus.server.domain.concert.entity.Seat;
import io.hhplus.server.domain.concert.repository.ConcertRepository;
import io.hhplus.server.domain.concert.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ConcertPlaceManager {

    private final ConcertRepository concertRepository;
    private final PlaceRepository placeRepository;

    /* 콘서트 공연장 전체 좌석 조회 */
    public List<Seat> getSeatsByConcertId(Long concertId) {
        Concert concert = concertRepository.findById(concertId);
        return placeRepository.findById(concert.getPlaceId()).getSeatList();
    }
}
