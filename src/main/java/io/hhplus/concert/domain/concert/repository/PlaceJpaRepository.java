package io.hhplus.concert.domain.concert.repository;

import io.hhplus.concert.domain.concert.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceJpaRepository extends JpaRepository<Place, Long> {
}
