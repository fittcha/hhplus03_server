package io.hhplus.server.concert.domain.repository;

import io.hhplus.server.concert.domain.entity.ConcertDate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertDateJpaRepository extends JpaRepository<ConcertDate, Long> {
}
