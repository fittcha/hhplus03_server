package io.hhplus.server.domain.concert.repository;

import io.hhplus.server.domain.concert.entity.Place;
import io.hhplus.server.domain.concert.entity.Seat;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceRepository {

    Place findById(Long placeId);

    Seat findSeatById(Long seatId);
}
