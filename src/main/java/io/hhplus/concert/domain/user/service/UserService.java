package io.hhplus.concert.domain.user.service;

import io.hhplus.concert.base.redis.RedissonLock;
import io.hhplus.concert.controller.user.dto.request.ChargeRequest;
import io.hhplus.concert.controller.user.dto.request.UseRequest;
import io.hhplus.concert.controller.user.dto.response.GetBalanceResponse;
import io.hhplus.concert.domain.user.entity.Users;
import io.hhplus.concert.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class UserService implements UserInterface {

    private final UserRepository userRepository;
    private final UserValidator userValidator;

    @Override
    public GetBalanceResponse getBalance(Long userId) {
        Users users = userRepository.findById(userId);
        return GetBalanceResponse.from(users);
    }

    @Override
    @Transactional
    @RedissonLock(key = "'userLock'.concat(':').concat(#userId)")
    public GetBalanceResponse charge(Long userId, ChargeRequest request) {
        Users users = userRepository.findById(userId);
        users = users.chargeBalance(BigDecimal.valueOf(request.amount()));
        return GetBalanceResponse.from(users);
    }

    @Override
    @Transactional
    @RedissonLock(key = "'userLock'.concat(':').concat(#userId)")
    public GetBalanceResponse use(Long userId, UseRequest request) {
        Users users = userRepository.findById(userId);

        // validate
        userValidator.insufficientBalance(users.getBalance(), BigDecimal.valueOf(request.amount()));

        users = users.useBalance(BigDecimal.valueOf(request.amount()));
        return GetBalanceResponse.from(users);
    }
}
