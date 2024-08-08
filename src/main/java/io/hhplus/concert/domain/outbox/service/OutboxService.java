package io.hhplus.concert.domain.outbox.service;

import io.hhplus.concert.domain.outbox.entity.Outbox;
import io.hhplus.concert.domain.outbox.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxService {

    private final OutboxRepository outboxRepository;

    public Outbox save(Outbox outbox) {
        return outboxRepository.save(outbox);
    }

    public Outbox findById(Long sendId) {
        Outbox outbox = outboxRepository.findById(sendId);
        if (outbox == null) {
            throw new RuntimeException("Not found send. sendId: " + sendId);
        }
        return outbox;
    }

    public void updateStatus(Outbox outbox, Outbox.Status status) {
        outbox.updateStatus(status);
    }

    public List<Outbox> findAllByStatus(Outbox.Status status) {
        return outboxRepository.findAllByStatus(status);
    }

    public void toPublished(Long outboxId) {
        Outbox outbox = findById(outboxId);
        outbox.updateStatus(Outbox.Status.DONE);
    }

    public void toRetry(Long outboxId) {
        Outbox outbox = findById(outboxId);
        outbox.updateStatus(Outbox.Status.RETRY);
    }
}

