package io.hhplus.server.domain.place.repository;

import io.hhplus.server.domain.place.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceJpaRepository extends JpaRepository<Place, Long> {
}
