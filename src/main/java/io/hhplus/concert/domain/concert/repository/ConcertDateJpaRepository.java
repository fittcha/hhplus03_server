package io.hhplus.concert.domain.concert.repository;

import io.hhplus.concert.domain.concert.entity.ConcertDate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertDateJpaRepository extends JpaRepository<ConcertDate, Long> {
}
