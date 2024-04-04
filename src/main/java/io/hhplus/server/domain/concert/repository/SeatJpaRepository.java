package io.hhplus.server.domain.concert.repository;

import io.hhplus.server.domain.concert.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatJpaRepository extends JpaRepository<Seat, Long> {
}
