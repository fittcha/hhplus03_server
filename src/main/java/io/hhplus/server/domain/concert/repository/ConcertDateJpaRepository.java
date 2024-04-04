package io.hhplus.server.domain.concert.repository;

import io.hhplus.server.domain.concert.entity.ConcertDate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertDateJpaRepository extends JpaRepository<ConcertDate, Long> {
}
