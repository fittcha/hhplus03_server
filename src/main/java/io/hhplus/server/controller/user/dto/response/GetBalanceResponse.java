package io.hhplus.server.controller.user.dto.response;

import io.hhplus.server.domain.user.entity.User;

import java.math.BigDecimal;

public record GetBalanceResponse(
        Long userId,
        BigDecimal balance
) {
    public static GetBalanceResponse from(User user) {
        return new GetBalanceResponse(user.getUserId(), user.getBalance());
    }
}
