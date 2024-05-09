package io.hhplus.server.domain.user.service;

import io.hhplus.server.base.redis.RedissonLock;
import io.hhplus.server.controller.user.dto.request.ChargeRequest;
import io.hhplus.server.controller.user.dto.request.UseRequest;
import io.hhplus.server.controller.user.dto.response.GetBalanceResponse;
import io.hhplus.server.domain.user.entity.Users;
import io.hhplus.server.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class UserService implements UserInterface {

    private final UserRepository userRepository;
    private final UserValidator userValidator;

    @Override
    @Transactional
    public GetBalanceResponse getBalance(Long userId) {
        Users users = userRepository.findById(userId);
        return GetBalanceResponse.from(users);
    }

    @Override
    @Transactional
    @RedissonLock(key = "'userLock'.concat(':').concat(#userId)")
    public GetBalanceResponse charge(Long userId, ChargeRequest request) {
        Users users = userRepository.findByIdWithPessimisticLock(userId);
        users = users.chargeBalance(BigDecimal.valueOf(request.amount()));
        return GetBalanceResponse.from(users);
    }

    @Override
    @Transactional
    @RedissonLock(key = "'userLock'.concat(':').concat(#userId)")
    public GetBalanceResponse use(Long userId, UseRequest request) {
        Users users = userRepository.findByIdWithPessimisticLock(userId);

        userValidator.insufficientBalance(users.getBalance(), BigDecimal.valueOf(request.amount()));

        users = users.useBalance(BigDecimal.valueOf(request.amount()));
        return GetBalanceResponse.from(users);
    }
}
