package io.hhplus.server.concert.domain.repository;

import io.hhplus.server.concert.domain.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatJpaRepository extends JpaRepository<Seat, Long> {
}
