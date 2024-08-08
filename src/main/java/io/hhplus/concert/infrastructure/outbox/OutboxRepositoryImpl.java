package io.hhplus.concert.infrastructure.outbox;

import io.hhplus.concert.domain.outbox.entity.Outbox;
import io.hhplus.concert.domain.outbox.repository.OutboxJpaRepository;
import io.hhplus.concert.domain.outbox.repository.OutboxRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OutboxRepositoryImpl implements OutboxRepository {

    private final OutboxJpaRepository outboxJpaRepository;

    public OutboxRepositoryImpl(OutboxJpaRepository outboxJpaRepository) {
        this.outboxJpaRepository = outboxJpaRepository;
    }

    @Override
    public Outbox save(Outbox outbox) {
        return outboxJpaRepository.save(outbox);
    }

    @Override
    public Outbox findById(Long sendId) {
        return outboxJpaRepository.findById(sendId).orElse(null);
    }

    @Override
    public List<Outbox> findAllByStatus(Outbox.Status status) {
        return outboxJpaRepository.findAllByStatus(status);
    }
}
