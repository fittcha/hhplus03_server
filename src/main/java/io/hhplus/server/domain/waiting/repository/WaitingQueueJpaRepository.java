package io.hhplus.server.domain.waiting.repository;

import io.hhplus.server.domain.waiting.entity.WaitingQueue;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface WaitingQueueJpaRepository extends JpaRepository<WaitingQueue, Long> {

    WaitingQueue findByUserId(Long userId);

    long countByStatusIs(WaitingQueue.Status status);

    WaitingQueue findByUserIdAndToken(Long userId, String token);

    List<WaitingQueue> findAllByRequestTimeBeforeAndStatusIs(Timestamp expireBefore, WaitingQueue.Status status);

    List<WaitingQueue> findByStatusIsOrderByRequestTimeAsc(WaitingQueue.Status status, Pageable pageable);
}
