package io.hhplus.server.domain.user.service;

import io.hhplus.server.controller.user.dto.request.ChargeRequest;
import io.hhplus.server.controller.user.dto.response.GetBalanceResponse;
import io.hhplus.server.controller.user.dto.response.GetMyReservationsResponse;

import java.util.List;

public interface UserInterface {

    // 잔액 조회
    GetBalanceResponse getBalance(Long userId);

    // 잔액 충전
    void charge(Long userId, ChargeRequest request);

    // 나의 예약 내역 조회
    List<GetMyReservationsResponse> getMyReservations(Long userId);
}
