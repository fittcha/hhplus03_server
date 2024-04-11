package io.hhplus.server.domain.waiting.service;

import io.hhplus.server.domain.waiting.WaitingConstants;
import io.hhplus.server.domain.waiting.entity.WaitingQueue;
import io.hhplus.server.domain.waiting.repository.WaitingQueueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

@Component
@RequiredArgsConstructor
public class WaitingScheduler {

    private final WaitingQueueRepository waitingQueueRepository;

    @Scheduled(fixedRate = 2000) // 매 2초마다 실행
    public void expireWaitingQueue() {
        // 현재 시간 기준으로 자동 만료 시간(5분) 전 시간 계산
        Timestamp expireBefore = new Timestamp(System.currentTimeMillis() - WaitingConstants.AUTO_EXPIRED_MILLIS);

        // 만료되어야 하는 대기열 항목 조회
        List<WaitingQueue> expiredTargets = waitingQueueRepository.findAllByRequestTimeBeforeAndStatusIs(expireBefore, WaitingQueue.Status.ACTIVE);

        // 만료 처리
        for (WaitingQueue entry : expiredTargets) {
            entry.expiredToken();
            waitingQueueRepository.save(entry);
        }

        // 다음 n개 순번 사용자 활성화 로직 호출
        activateNextUser(expiredTargets.size());
    }

    public void activateNextUser(int cnt) {
        List<WaitingQueue> waitingUsers = waitingQueueRepository.findByStatusIsOrderByRequestTimeAsc(WaitingQueue.Status.WAITING, PageRequest.of(0, cnt));

        if (!waitingUsers.isEmpty()) {
            // 다음 순번 유저를 활성화
            waitingUsers.forEach(WaitingQueue::toActive);
        }
    }

}
