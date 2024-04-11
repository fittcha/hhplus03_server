package io.hhplus.server.domain.user.service;

import io.hhplus.server.controller.user.dto.request.ChargeRequest;
import io.hhplus.server.controller.user.dto.response.GetBalanceResponse;

public interface UserInterface {

    /* 잔액 조회 */
    GetBalanceResponse getBalance(Long userId);

    /* 잔액 충전 */
    GetBalanceResponse charge(Long userId, ChargeRequest request);
}
