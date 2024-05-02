package io.hhplus.server.domain.user.service;

import io.hhplus.server.controller.user.dto.request.ChargeRequest;
import io.hhplus.server.controller.user.dto.request.UseRequest;
import io.hhplus.server.controller.user.dto.response.GetBalanceResponse;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserFacade implements UserInterface {

    private final UserService userService;
    private static final int MAX_RETRIES = 5;

    @Override
    public GetBalanceResponse getBalance(Long userId) {
        return null;
    }

    @Override
    public GetBalanceResponse charge(Long userId, ChargeRequest request) {
        return null;
    }

    @Override
    public GetBalanceResponse use(Long userId, UseRequest request) {
        return null;
    }

    @Override
    public GetBalanceResponse chargeWithRetry(Long userId, ChargeRequest request) {
        int attempt = 0;
        while (true) {
            try {
                return userService.charge(userId, request);
            } catch (ObjectOptimisticLockingFailureException | OptimisticLockException ex) {
                if (++attempt >= MAX_RETRIES) throw ex;
                log.info(":: user charge 충돌 감지, 재시도 시도: {}", attempt);
            }
        }
    }

    @Override
    public GetBalanceResponse useWithRetry(Long userId, UseRequest request) {
        int attempt = 0;
        while (true) {
            try {
                return userService.use(userId, request);
            } catch (ObjectOptimisticLockingFailureException | OptimisticLockException ex) {
                if (++attempt >= MAX_RETRIES) throw ex;
                log.info(":: user use 충돌 감지, 재시도 시도: {}", attempt);
            }
        }
    }
}
