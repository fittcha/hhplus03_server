package io.hhplus.server.domain.outbox.repository;

import io.hhplus.server.domain.outbox.entity.Outbox;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutboxJpaRepository extends JpaRepository<Outbox, Long> {

}
