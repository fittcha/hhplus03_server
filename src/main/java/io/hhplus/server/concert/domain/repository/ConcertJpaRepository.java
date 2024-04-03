package io.hhplus.server.concert.domain.repository;

import io.hhplus.server.concert.domain.entity.Concert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertJpaRepository extends JpaRepository<Concert, Long> {
}
