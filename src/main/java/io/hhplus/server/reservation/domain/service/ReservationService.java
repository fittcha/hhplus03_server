package io.hhplus.server.reservation.domain.service;

import io.hhplus.server.reservation.controller.dto.request.CancelRequest;
import io.hhplus.server.reservation.controller.dto.request.ReserveRequest;
import io.hhplus.server.reservation.controller.dto.response.ReserveResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationService implements ReservationInterface {

    @Override
    public ReserveResponse reserve(ReserveRequest request) {
        return null;
    }

    @Override
    public void cancel(CancelRequest request) {

    }
}
