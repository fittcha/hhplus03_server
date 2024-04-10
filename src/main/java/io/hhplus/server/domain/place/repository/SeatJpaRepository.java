package io.hhplus.server.domain.place.repository;

import io.hhplus.server.domain.place.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatJpaRepository extends JpaRepository<Seat, Long> {
}
