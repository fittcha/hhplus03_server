package io.hhplus.server.domain.concert.repository;

import io.hhplus.server.domain.concert.entity.Place;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceRepository {

    Place findById(Long placeId);

    void addPlace(Place place);
}
