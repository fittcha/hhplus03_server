package io.hhplus.server.domain.concert.repository;

import io.hhplus.server.domain.concert.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceJpaRepository extends JpaRepository<Place, Long> {
}
