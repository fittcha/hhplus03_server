package io.hhplus.server.domain.place.service;

import io.hhplus.server.domain.place.entity.Place;
import io.hhplus.server.domain.place.entity.Seat;
import io.hhplus.server.domain.place.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceService implements PlaceInterface {

    private final PlaceRepository placeRepository;

    @Override
    public List<Seat> getSeatsByPlace(Long placeId) {
        return placeRepository.findById(placeId).getSeatList();
    }

    public Place getPlace(Long placeId) {
        return placeRepository.findById(placeId);
    }
}
