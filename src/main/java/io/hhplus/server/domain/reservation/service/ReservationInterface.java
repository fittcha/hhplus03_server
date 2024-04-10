package io.hhplus.server.domain.reservation.service;

import io.hhplus.server.controller.reservation.dto.request.CancelRequest;
import io.hhplus.server.controller.reservation.dto.request.ReserveRequest;
import io.hhplus.server.controller.reservation.dto.response.ReserveResponse;
import io.hhplus.server.domain.reservation.entity.Reservation;

import java.util.List;

public interface ReservationInterface {

    // 콘서트 좌석 예매
    ReserveResponse reserve(ReserveRequest request);

    // 좌석 예매 취소
    void cancel(Long reservationId, CancelRequest request);

    // 콘서트 회차별 예매정보 조회
    List<Reservation> getReservationsByConcertDate(Long concertDateId);
}
