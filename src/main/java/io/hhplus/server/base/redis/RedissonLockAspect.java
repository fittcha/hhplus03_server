package io.hhplus.server.base.redis;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Aspect
@Component
public class RedissonLockAspect {

    private final RedissonClient redissonClient;

    public RedissonLockAspect(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Around("@annotation(redissonLock)")
    public Object around(ProceedingJoinPoint joinPoint, RedissonLock redissonLock) throws Throwable {
        RLock lock = redissonClient.getLock(redissonLock.value());
        try {
            // 락 취득 시도
            boolean isLocked = lock.tryLock(10, 60, TimeUnit.SECONDS);
            if (!isLocked) {
                throw new IllegalStateException("Unable to acquire lock");
            }
            return joinPoint.proceed();
        } finally {
            lock.unlock();
        }
    }
}
