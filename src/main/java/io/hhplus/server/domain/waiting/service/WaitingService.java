package io.hhplus.server.domain.waiting.service;

import io.hhplus.server.base.jwt.JwtService;
import io.hhplus.server.base.redis.dto.RedisZSetReqDto;
import io.hhplus.server.base.redis.service.RedisZSetService;
import io.hhplus.server.controller.waiting.dto.response.CheckWaitingResponse;
import io.hhplus.server.domain.waiting.WaitingConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WaitingService implements WaitingInterface {

    private final RedisZSetService redisZSetService;
    private final JwtService jwtService;

    @Override
    public String issueToken(Long userId) {
        return jwtService.createToken(userId);
    }

    @Override
    @Transactional
    public CheckWaitingResponse checkWaiting(Long userId, String reqToken) {
        String token = reqToken;

        // 기존 토큰이 없으면 새로 발급
        if (reqToken != null) {
            token = issueToken(userId);
        }

        // 활성 유저 수 확인
        Long activeTokenCnt = redisZSetService.zSetSize(new RedisZSetReqDto.ZCard(WaitingConstants.ACTIVE_KEY));
        // 진입 가능
        if (activeTokenCnt < WaitingConstants.MAX_ACTIVE_USER) {
            return getInActive(token);
        }

        // 진입 불가능 > 대기열 정보 반환
        return getInWaiting(token);
    }

    private CheckWaitingResponse getInWaiting(String token) {
        Long waitingNum;
        long waitTimeInSeconds;

        // 나의 대기열 확인
        Long myRank = redisZSetService.zSetRank(new RedisZSetReqDto.ZRank(WaitingConstants.WAIT_KEY, token));
        if (myRank == null) { // 대기열 없을 경우 > 진입 후 대기 순번 확인
            redisZSetService.zSetAdd(new RedisZSetReqDto.ZAdd(WaitingConstants.WAIT_KEY, System.currentTimeMillis(), token));
            waitingNum = redisZSetService.zSetRank(new RedisZSetReqDto.ZRank(WaitingConstants.WAIT_KEY, token));
        } else { // 대기열 있을 경우
            waitingNum = myRank;
        }
        // 대기 잔여 시간 계산 (10초당 활성 전환 수)
        waitTimeInSeconds = (long) Math.ceil((double) (waitingNum - 1) / WaitingConstants.ENTER_10_SECONDS) * 10;

        return new CheckWaitingResponse(
                token,
                false,
                new CheckWaitingResponse.WaitingTicketInfo(waitingNum, waitTimeInSeconds)
        );
    }

    private CheckWaitingResponse getInActive(String token) {
        // 활성 만료 시간
        long expiredTimeMillis = System.currentTimeMillis() + WaitingConstants.AUTO_EXPIRED_TIME;
        // 활성 유저열에 추가
        redisZSetService.zSetAdd(new RedisZSetReqDto.ZAdd(WaitingConstants.ACTIVE_KEY, expiredTimeMillis, token));
        // 대기열에서 사용자 토큰 삭제
        redisZSetService.zSetRemove(new RedisZSetReqDto.ZRem(WaitingConstants.WAIT_KEY, token));

        return new CheckWaitingResponse(token, true, null);
    }
}
