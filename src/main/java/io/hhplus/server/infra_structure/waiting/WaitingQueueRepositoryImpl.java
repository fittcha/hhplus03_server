package io.hhplus.server.infra_structure.waiting;

import io.hhplus.server.domain.waiting.entity.WaitingQueue;
import io.hhplus.server.domain.waiting.repository.WaitingQueueJpaRepository;
import io.hhplus.server.domain.waiting.repository.WaitingQueueRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public class WaitingQueueRepositoryImpl implements WaitingQueueRepository {

    private final WaitingQueueJpaRepository waitingQueueJpaRepository;

    public WaitingQueueRepositoryImpl(WaitingQueueJpaRepository waitingQueueJpaRepository) {
        this.waitingQueueJpaRepository = waitingQueueJpaRepository;
    }

    @Override
    public WaitingQueue findByUserId(Long userId) {
        return waitingQueueJpaRepository.findByUserId(userId);
    }

    @Override
    public long countByStatusIs(WaitingQueue.Status status) {
        return waitingQueueJpaRepository.countByStatusIs(status);
    }

    @Override
    public long countByRequestTimeBeforeAndStatusIs(WaitingQueue.Status status, Timestamp requestTime) {
        return waitingQueueJpaRepository.countByRequestTimeBeforeAndStatusIs(requestTime, status);
    }

    @Override
    public void save(WaitingQueue waitingQueue) {
        waitingQueueJpaRepository.save(waitingQueue);
    }

    @Override
    public WaitingQueue findByUserIdAndToken(Long userId, String token) {
        return waitingQueueJpaRepository.findByUserIdAndToken(userId, token);
    }

    @Override
    public List<WaitingQueue> findAllByRequestTimeBeforeAndStatusIs(Timestamp expireBefore, WaitingQueue.Status status) {
        return waitingQueueJpaRepository.findAllByRequestTimeBeforeAndStatusIs(expireBefore, status);
    }

    @Override
    public List<WaitingQueue> findByStatusIsOrderByRequestTimeAsc(WaitingQueue.Status status, PageRequest pageRequest) {
        return waitingQueueJpaRepository.findByStatusIsOrderByRequestTimeAsc(status, pageRequest);
    }
}
