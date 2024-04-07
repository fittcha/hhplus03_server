package io.hhplus.server.domain.user.service;

import io.hhplus.server.controller.user.dto.request.ChargeRequest;
import io.hhplus.server.controller.user.dto.response.GetBalanceResponse;
import io.hhplus.server.controller.user.dto.response.GetMyReservationsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserInterface {

    @Override
    public GetBalanceResponse getBalance(Long userId) {
        return null;
    }

    @Override
    public void charge(Long userId, ChargeRequest request) {

    }

    @Override
    public List<GetMyReservationsResponse> getMyReservations(Long userId) {
        return null;
    }
}
