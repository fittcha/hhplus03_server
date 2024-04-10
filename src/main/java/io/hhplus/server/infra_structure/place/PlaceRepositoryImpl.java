package io.hhplus.server.infra_structure.place;

import io.hhplus.server.domain.place.entity.Place;
import io.hhplus.server.domain.place.repository.PlaceJpaRepository;
import io.hhplus.server.domain.place.repository.PlaceRepository;
import org.springframework.stereotype.Repository;

import java.util.NoSuchElementException;

@Repository
public class PlaceRepositoryImpl implements PlaceRepository {

    private final PlaceJpaRepository placeJpaRepository;

    public PlaceRepositoryImpl(PlaceJpaRepository placeJpaRepository) {
        this.placeJpaRepository = placeJpaRepository;
    }

    @Override
    public Place findById(Long placeId) {
        return placeJpaRepository.findById(placeId).orElseThrow(NoSuchElementException::new);
    }
}
