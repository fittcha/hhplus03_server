package io.hhplus.concert.domain.outbox.repository;

import io.hhplus.concert.domain.outbox.entity.Outbox;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutboxRepository {

    Outbox save(Outbox outbox);

    Outbox findById(Long sendId);

    List<Outbox> findAllByStatus(Outbox.Status status);
}
