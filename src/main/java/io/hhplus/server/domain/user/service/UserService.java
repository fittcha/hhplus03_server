package io.hhplus.server.domain.user.service;

import io.hhplus.server.controller.user.dto.request.ChargeRequest;
import io.hhplus.server.controller.user.dto.response.GetBalanceResponse;
import io.hhplus.server.domain.user.entity.User;
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
        User user = userRepository.findById(userId);
        return GetBalanceResponse.from(user);
    }

    @Override
    public GetBalanceResponse charge(Long userId, ChargeRequest request) {
        User user = userRepository.findById(userId);
        user = user.chargeBalance(BigDecimal.valueOf(request.amount()));
        return GetBalanceResponse.from(user);
    }
}
