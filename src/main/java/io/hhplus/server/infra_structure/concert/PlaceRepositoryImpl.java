package io.hhplus.server.infra_structure.concert;

import io.hhplus.server.domain.concert.entity.Place;
import io.hhplus.server.domain.concert.entity.Seat;
import io.hhplus.server.domain.concert.repository.PlaceJpaRepository;
import io.hhplus.server.domain.concert.repository.PlaceRepository;
import io.hhplus.server.domain.concert.repository.SeatJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Repository;

@Repository
public class PlaceRepositoryImpl implements PlaceRepository {

    private final PlaceJpaRepository placeJpaRepository;
    private final SeatJpaRepository seatJpaRepository;

    public PlaceRepositoryImpl(PlaceJpaRepository placeJpaRepository, SeatJpaRepository seatJpaRepository) {
        this.placeJpaRepository = placeJpaRepository;
        this.seatJpaRepository = seatJpaRepository;
    }

    @Override
    public Place findById(Long placeId) {
        return placeJpaRepository.findById(placeId).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Seat findSeatById(Long seatId) {
        return seatJpaRepository.findById(seatId).orElseThrow(EntityNotFoundException::new);
    }
}
