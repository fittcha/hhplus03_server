package io.hhplus.concert.domain.reservation.service;

import io.hhplus.concert.controller.reservation.dto.request.CancelRequest;
import io.hhplus.concert.controller.reservation.dto.request.ReserveRequest;
import io.hhplus.concert.controller.reservation.dto.response.ReserveResponse;
import io.hhplus.concert.controller.user.dto.response.GetMyReservationsResponse;

import java.util.List;

public interface ReservationInterface {

    /* 콘서트 좌석 예매 */
    ReserveResponse reserve(ReserveRequest request);

    /* 좌석 예매 취소 */
    void cancel(Long reservationId, CancelRequest request);

    /* 나의 예약 내역 조회 */
    List<GetMyReservationsResponse> getMyReservations(Long userId);
}
