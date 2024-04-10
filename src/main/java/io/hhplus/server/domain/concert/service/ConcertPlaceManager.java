package io.hhplus.server.domain.concert.service;

import io.hhplus.server.domain.concert.entity.Concert;
import io.hhplus.server.domain.concert.entity.Place;
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

    public List<Seat> getSeatsByPlace(Long concertId) {
        Concert concert = concertRepository.findById(concertId);
        return placeRepository.findById(concert.getPlaceId()).getSeatList();
    }

    public Place getPlace(Long placeId) {
        return placeRepository.findById(placeId);
    }
}
