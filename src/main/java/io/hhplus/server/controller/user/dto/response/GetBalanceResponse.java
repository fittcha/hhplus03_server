package io.hhplus.server.controller.user.dto.response;

import io.hhplus.server.domain.user.entity.Users;

import java.math.BigDecimal;

public record GetBalanceResponse(
        Long userId,
        BigDecimal balance
) {
    public static GetBalanceResponse from(Users users) {
        return new GetBalanceResponse(users.getUserId(), users.getBalance());
    }
}
