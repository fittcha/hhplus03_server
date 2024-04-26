package io.hhplus.server.domain.user.service;

import io.hhplus.server.controller.user.dto.request.ChargeRequest;
import io.hhplus.server.controller.user.dto.response.GetBalanceResponse;
import io.hhplus.server.domain.user.entity.Users;
import io.hhplus.server.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class UserService implements UserInterface {

    private final UserRepository userRepository;

    @Override
    public GetBalanceResponse getBalance(Long userId) {
        Users users = userRepository.findById(userId);
        return GetBalanceResponse.from(users);
    }

    @Override
    public GetBalanceResponse charge(Long userId, ChargeRequest request) {
        Users users = userRepository.findById(userId);
        users = users.chargeBalance(BigDecimal.valueOf(request.amount()));
        return GetBalanceResponse.from(users);
    }
}
