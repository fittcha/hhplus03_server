package io.hhplus.server.domain.waiting.service;

import io.hhplus.server.base.jwt.JwtService;
import io.hhplus.server.controller.waiting.dto.response.CheckActiveResponse;
import io.hhplus.server.controller.waiting.dto.response.IssueTokenResponse;
import io.hhplus.server.domain.waiting.WaitingConstants;
import io.hhplus.server.domain.waiting.entity.WaitingQueue;
import io.hhplus.server.domain.waiting.repository.WaitingQueueRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class WaitingService implements WaitingInterface {

    private final WaitingQueueRepository waitingQueueRepository;
    private final JwtService jwtService;

    @Override
    public IssueTokenResponse issueToken(Long userId) {
        return new IssueTokenResponse(jwtService.createToken(userId));
    }

    @Override
    public CheckActiveResponse addWaitingQueue(Long userId, String token) {
        Long waitingNum = null;
        Long expectedWaitTimeInSeconds = null;

        // 기존 토큰 있으면 만료시킴
        expiredIfExist(userId);

        // 대기열 활성 유저 수 확인
        long activeSize = waitingQueueRepository.countByStatusIs(WaitingQueue.Status.ACTIVE);
        boolean isActive = activeSize < WaitingConstants.ACTIVE_USER_CNT;
        if (isActive) {
            // 유저 진입 활성화
            waitingQueueRepository.save(WaitingQueue.toActiveEntity(userId, token));
        } else {
            // 유저 비활성, 대기열 정보 생성
            waitingNum = activeSize - WaitingConstants.ACTIVE_USER_CNT;
            expectedWaitTimeInSeconds = Duration.ofMinutes(waitingNum).toSeconds();
            waitingQueueRepository.save(WaitingQueue.toWaitingEntity(userId, token));
        }

        return new CheckActiveResponse(
                isActive,
                new CheckActiveResponse.WaitingTicketInfo(waitingNum, expectedWaitTimeInSeconds)
        );
    }

    public void expiredIfExist(Long userId) {
        WaitingQueue existingQueue = waitingQueueRepository.findByUserId(userId);
        if (existingQueue != null) {
            existingQueue.expiredToken();
        }
    }

    @Override
    public CheckActiveResponse checkActive(Long userId, String token) {
        Long waitingNum = null;
        Long expectedWaitTimeInSeconds = null;

        // 내 대기 상태 확인
        WaitingQueue waitingQueue = waitingQueueRepository.findByUserIdAndToken(userId, token);
        if (waitingQueue == null || waitingQueue.getStatus().equals(WaitingQueue.Status.EXPIRED)) {
            throw new EntityNotFoundException("새로고침하여 다시 진입하세요.");
        }

        // 활성 여부, 대기열 정보 반환
        boolean isActive = waitingQueue.getStatus().equals(WaitingQueue.Status.ACTIVE);
        if (!isActive) {
            // 대기열 정보 생성
            long activeSize = waitingQueueRepository.countByStatusIs(WaitingQueue.Status.ACTIVE);
            waitingNum = activeSize - WaitingConstants.ACTIVE_USER_CNT;
            expectedWaitTimeInSeconds = Duration.ofMinutes(waitingNum).toSeconds();
        }

        return new CheckActiveResponse(
                isActive,
                new CheckActiveResponse.WaitingTicketInfo(waitingNum, expectedWaitTimeInSeconds)
        );
    }


}
