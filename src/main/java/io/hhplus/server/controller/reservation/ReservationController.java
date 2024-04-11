package io.hhplus.server.controller.reservation;

import io.hhplus.server.base.exception.ApiResult;
import io.hhplus.server.controller.reservation.dto.request.CancelRequest;
import io.hhplus.server.controller.reservation.dto.request.ReserveRequest;
import io.hhplus.server.controller.reservation.dto.response.ReserveResponse;
import io.hhplus.server.controller.user.dto.response.GetMyReservationsResponse;
import io.hhplus.server.domain.payment.PaymentEnums;
import io.hhplus.server.domain.reservation.ReservationEnums;
import io.hhplus.server.domain.reservation.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

@Tag(name = "예약", description = "reservation-controller")
@RequestMapping("/reservations")
@RestController
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService service;

    @Operation(summary = "예약")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = ReserveResponse.class)))
    @PostMapping("")
    public ApiResult<ReserveResponse> reserve(@RequestBody @Valid ReserveRequest request) {
        // dummy data
        return ApiResult.success(ReserveResponse.builder()
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
                        .paymentPrice(BigDecimal.valueOf(79000))
                        .build())
                .build());
    }

    @Operation(summary = "예약 취소")
    @ApiResponse(responseCode = "200", description = "OK")
    @DeleteMapping("/{reservationId}")
    public ApiResult<Void> cancel(@PathVariable(value = "reservationId") Long reservationId,
                       @RequestBody @Valid CancelRequest request) {
        return ApiResult.successNoContent();
    }

    @Operation(summary = "나의 예약 내역 조회")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(array = @ArraySchema(schema = @Schema(implementation = GetMyReservationsResponse.class))))
    @GetMapping("/me")
    public List<GetMyReservationsResponse> getMyReservation(@RequestParam(value = "userId") @NotNull Long userId) {
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
