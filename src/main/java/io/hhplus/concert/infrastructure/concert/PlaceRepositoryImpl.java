package io.hhplus.concert.infrastructure.concert;

import io.hhplus.concert.domain.concert.entity.Place;
import io.hhplus.concert.domain.concert.repository.PlaceJpaRepository;
import io.hhplus.concert.domain.concert.repository.PlaceRepository;
import io.hhplus.concert.domain.concert.repository.SeatJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Repository;

@Repository
public class PlaceRepositoryImpl implements PlaceRepository {

    private final PlaceJpaRepository placeJpaRepository;

    public PlaceRepositoryImpl(PlaceJpaRepository placeJpaRepository, SeatJpaRepository seatJpaRepository) {
        this.placeJpaRepository = placeJpaRepository;
    }

    @Override
    public Place findById(Long placeId) {
        return placeJpaRepository.findById(placeId).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public void addPlace(Place place) {
        placeJpaRepository.save(place);
    }
}
