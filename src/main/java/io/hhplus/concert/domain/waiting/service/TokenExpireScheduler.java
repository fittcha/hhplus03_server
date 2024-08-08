package io.hhplus.concert.domain.waiting.service;

import io.hhplus.concert.base.redis.dto.RedisZSetReqDto;
import io.hhplus.concert.base.redis.service.RedisZSetService;
import io.hhplus.concert.domain.waiting.WaitingConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@EnableScheduling
public class TokenExpireScheduler {

    private final RedisZSetService redisZSetService;

    // 매 1분마다 실행
    @Scheduled(cron = "0 * * * * *")
    public void removeExpiredTokens() {
        // 현재 시간 이전에 만료된 토큰을 삭제
        long currentTime = System.currentTimeMillis() / 1000;
        Long deletedCount = redisZSetService.zSetRemoveRangeByScore(new RedisZSetReqDto.ZRemoveRangeByScore(WaitingConstants.ACTIVE_KEY, 0, currentTime));
        log.info("Removed expired tokens: {}", deletedCount);
    }
}
