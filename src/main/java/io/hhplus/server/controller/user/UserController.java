package io.hhplus.server.controller.user;

import io.hhplus.server.controller.user.dto.request.ChargeRequest;
import io.hhplus.server.controller.user.dto.response.GetBalanceResponse;
import io.hhplus.server.controller.user.dto.response.GetMyReservationsResponse;
import io.hhplus.server.domain.payment.PaymentEnums;
import io.hhplus.server.domain.reservation.ReservationEnums;
import io.hhplus.server.domain.user.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

@RequestMapping("/users")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping("/{userId}/balance")
    public GetBalanceResponse getBalance(@PathVariable(value = "userId") @NotNull Long userId) {
        // dummy data
        return new GetBalanceResponse(BigDecimal.valueOf(1000));
    }

    @PatchMapping("/{userId}/charge")
    public void charge(@PathVariable(value = "userId") @NotNull Long userId,
                       @RequestBody @Valid ChargeRequest request) {

    }

    @GetMapping("/{userId}/reservation")
    public List<GetMyReservationsResponse> getMyReservation(@PathVariable(value = "userId") @NotNull Long userId) {
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
                        .paymentPrice(BigDecimal.valueOf(79000))
                        .build())
                .build());
    }
}
