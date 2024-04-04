package io.hhplus.server.domain.reservation.service;

import io.hhplus.server.controller.reservation.dto.request.CancelRequest;
import io.hhplus.server.controller.reservation.dto.request.ReserveRequest;
import io.hhplus.server.controller.reservation.dto.response.ReserveResponse;
import io.hhplus.server.domain.payment.PaymentEnums;
import io.hhplus.server.domain.reservation.ReservationEnums;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
public class ReservationService implements ReservationInterface {

    @Override
    public ReserveResponse reserve(ReserveRequest request) {
        // dummy data
        return ReserveResponse.builder()
                .reservationId(1L)
                .status(ReservationEnums.Status.ING)
                .concertInfo(ReserveResponse.ConcertInfo.builder()
                        .concertId(1L)
                        .concertDateId(1L)
                        .name("2024 테스트 콘서트 in 서울")
                        .date(ZonedDateTime.now().plusMonths(1))
                        .seatId(2L)
                        .seatNum(2)
                        .build())
                .paymentInfo(ReserveResponse.PaymentInfo.builder()
                        .paymentId(1L)
                        .status(PaymentEnums.Status.READY)
                        .paymentPrice(79000)
                        .build())
                .build();
    }

    @Override
    public void cancel(Long reservationId, CancelRequest request) {

    }
}
