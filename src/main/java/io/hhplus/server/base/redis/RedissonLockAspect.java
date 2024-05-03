package io.hhplus.server.base.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class RedissonLockAspect {

    private final RedissonClient redissonClient;

    @Around("@annotation(io.hhplus.server.base.redis.RedissonLock)")
    public Object lock(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RedissonLock redissonLock = method.getAnnotation(RedissonLock.class);

        RLock lock = redissonClient.getLock(redissonLock.key());
        try {
            // 락 취득 시도
            boolean available  = lock.tryLock(redissonLock.waitTime(), redissonLock.leaseTime(), redissonLock.timeUnit());
            if (!available) {
                throw new IllegalStateException("Unable to acquire lock");
            }
            return joinPoint.proceed();
        } finally {
            lock.unlock();
        }
    }
}
