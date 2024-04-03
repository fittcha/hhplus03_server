package io.hhplus.server.reservation.domain.service;

import io.hhplus.server.reservation.controller.dto.request.CancelRequest;
import io.hhplus.server.reservation.controller.dto.request.ReserveRequest;
import io.hhplus.server.reservation.controller.dto.response.ReserveResponse;

public interface ReservationInterface {

    // 콘서트 좌석 예매
    ReserveResponse reserve(ReserveRequest request);

    // 좌석 예매 취소
    void cancel(CancelRequest request);
}
