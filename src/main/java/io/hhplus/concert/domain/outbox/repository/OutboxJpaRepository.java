package io.hhplus.concert.domain.outbox.repository;

import io.hhplus.concert.domain.outbox.entity.Outbox;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutboxJpaRepository extends JpaRepository<Outbox, Long> {

    List<Outbox> findAllByStatus(Outbox.Status status);
}
