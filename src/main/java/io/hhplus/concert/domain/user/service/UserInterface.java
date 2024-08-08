package io.hhplus.concert.domain.user.service;

import io.hhplus.concert.controller.user.dto.request.ChargeRequest;
import io.hhplus.concert.controller.user.dto.request.UseRequest;
import io.hhplus.concert.controller.user.dto.response.GetBalanceResponse;

public interface UserInterface {

    /* 잔액 조회 */
    GetBalanceResponse getBalance(Long userId);

    /* 잔액 충전 */
    GetBalanceResponse charge(Long userId, ChargeRequest request);

    /* 잔액 사용 */
    GetBalanceResponse use(Long userId, UseRequest request);
}
