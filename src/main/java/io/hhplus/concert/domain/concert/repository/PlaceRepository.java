package io.hhplus.concert.domain.concert.repository;

import io.hhplus.concert.domain.concert.entity.Place;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceRepository {

    Place findById(Long placeId);

    void addPlace(Place place);
}
