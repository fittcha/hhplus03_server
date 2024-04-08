package io.hhplus.server.infra_structure.concert;

import io.hhplus.server.domain.concert.entity.Place;
import io.hhplus.server.domain.concert.repository.PlaceJpaRepository;
import io.hhplus.server.domain.concert.repository.PlaceRepository;

import java.util.NoSuchElementException;

public class PlaceRepositoryImpl implements PlaceRepository {

    private PlaceJpaRepository placeJpaRepository;

    @Override
    public Place findById(Long placeId) {
        return placeJpaRepository.findById(placeId).orElseThrow(NoSuchElementException::new);
    }
}
