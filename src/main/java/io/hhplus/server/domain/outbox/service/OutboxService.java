package io.hhplus.server.domain.outbox.service;

import io.hhplus.server.domain.outbox.entity.Outbox;
import io.hhplus.server.domain.outbox.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OutboxService {

    private final OutboxRepository outboxRepository;

    public Outbox save(Outbox outbox) {
        return outboxRepository.save(outbox);
    }

    public Outbox findById(Long outboxId) {
        return outboxRepository.findById(outboxId);
    }

    public void toPublished(Long outboxId) {
        Outbox outbox = findById(outboxId);
        outbox.updateStatus(Outbox.Status.DONE);
    }
}
