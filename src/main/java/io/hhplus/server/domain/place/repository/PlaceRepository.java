package io.hhplus.server.domain.place.repository;

import io.hhplus.server.domain.place.entity.Place;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceRepository {

    Place findById(Long placeId);
}
