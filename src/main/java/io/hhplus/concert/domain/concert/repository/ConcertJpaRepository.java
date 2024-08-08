package io.hhplus.concert.domain.concert.repository;

import io.hhplus.concert.domain.concert.entity.Concert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertJpaRepository extends JpaRepository<Concert, Long> {
}
