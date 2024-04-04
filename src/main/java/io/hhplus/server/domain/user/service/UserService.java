package io.hhplus.server.domain.user.service;

import io.hhplus.server.controller.reservation.dto.response.ReserveResponse;
import io.hhplus.server.controller.user.dto.request.ChargeRequest;
import io.hhplus.server.controller.user.dto.response.GetBalanceResponse;
import io.hhplus.server.controller.user.dto.response.GetMyReservationsResponse;
import io.hhplus.server.domain.payment.PaymentEnums;
import io.hhplus.server.domain.reservation.ReservationEnums;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserInterface {

    @Override
    public GetBalanceResponse getBalance(Long userId) {
        // dummy data
        return new GetBalanceResponse(1000);
    }

    @Override
    public void charge(Long userId, ChargeRequest request) {

    }

    @Override
    public List<GetMyReservationsResponse> getMyReservations(Long userId) {
        // dummy data
        return List.of(GetMyReservationsResponse.builder()
                        .reservationId(1L)
                        .status(ReservationEnums.Status.ING)
                        .concertInfo(GetMyReservationsResponse.ConcertInfo.builder()
                                .concertId(1L)
                                .concertDateId(1L)
                                .name("2024 테스트 콘서트 in 서울")
                                .date(ZonedDateTime.now().plusMonths(1))
                                .seatId(2L)
                                .seatNum(2)
                                .build())
                        .paymentInfo(GetMyReservationsResponse.PaymentInfo.builder()
                                .paymentId(1L)
                                .status(PaymentEnums.Status.READY)
                                .paymentPrice(79000)
                                .build())
                        .build());
    }
}
