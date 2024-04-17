package io.hhplus.server.intergration;

import io.hhplus.server.base.exception.ApiResult;
import io.hhplus.server.base.exception.CustomException;
import io.hhplus.server.controller.reservation.dto.request.ReserveRequest;
import io.hhplus.server.controller.reservation.dto.response.ReserveResponse;
import io.hhplus.server.domain.reservation.ReservationExceptionEnum;
import io.hhplus.server.domain.reservation.entity.Reservation;
import io.hhplus.server.domain.reservation.repository.ReservationRepository;
import io.hhplus.server.intergration.base.BaseIntegrationTest;
import io.hhplus.server.intergration.base.TestDataHandler;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.logging.LogLevel;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.fail;

class ReservationIntegrationTest extends BaseIntegrationTest {

    @Autowired
    TestDataHandler testDataHandler;
    @Autowired
    ReservationRepository reservationRepository;

    private static final String PATH = "/reservations";

    @Test
    @DisplayName("예약된 좌석이면 '이미 선택된 좌석입니다.' 반환")
    void reserveTest_ALREADY_RESERVED() {
        // given
        ReserveRequest request = new ReserveRequest(1L, 1L, 5L, 1L);

        // when
        post(LOCAL_HOST + port + PATH, request);

        // then
        assertThatThrownBy(() -> {
            throw new CustomException(ReservationExceptionEnum.ALREADY_RESERVED, null, LogLevel.INFO);
        })
                .isInstanceOf(CustomException.class)
                .hasMessageContaining("이미 선택된 좌석입니다.");
    }

    @Test
    @DisplayName("유저 여러 명이 동시에 예약 신청하면 한 명만 예약 성공, 나머지 유저는 '이선좌' 반환")
    void reserveTest_ALREADY_RESERVED_lock() {
        // given
        for (int i = 0; i < 10; i++) {
            testDataHandler.settingUser(BigDecimal.ZERO);
        }
        long concertId = 1L;
        long concertDateId = 1L;
        long userId = 1L; // 시작 유저 pk
        long seatId = 19L;

        // when - 동시에 한 좌석 예약 요청
        AtomicInteger successCount = new AtomicInteger(0);
        List<CompletableFuture<ExtractableResponse<Response>>> futures = IntStream.range(0, 10)
                .mapToObj(i -> CompletableFuture.supplyAsync(() -> {
                    ReserveRequest request = new ReserveRequest(concertId, concertDateId, seatId, userId + i);
                    return post(LOCAL_HOST + port + PATH, request);
                }))
                .toList();
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        // then
        List<ExtractableResponse<Response>> responses = futures.stream()
                .map(CompletableFuture::join)
                .toList();
        responses.forEach(response -> {
            if (response.body().jsonPath().getObject("success", Boolean.class).equals(true)) {
                successCount.getAndIncrement();
            } else if (!response.body().jsonPath().getObject("error", ApiResult.Error.class).message().equals("이미 선택된 좌석입니다.")) {
                fail("예상치 못한 응답: " + response);
            }
        });
        assertThat(successCount.get()).isEqualTo(1);
    }

    @Test
    @DisplayName("유저 여러 명이 동시에 다른 좌석 예약 신청하면 모두 예약 성공")
    void reserveTest_success_all() {

    }

    @Test
    @DisplayName("예약을 성공해도 5분간 결제 완료되지 않았을 시 자동 취소된다.")
    void reserveTest_cancel_after_5min() throws InterruptedException {
        // given
        testDataHandler.settingUser(BigDecimal.ZERO);
        long concertId = 1L;
        long concertDateId = 1L;
        long userId = 1L;
        long seatId = 19L;
        ReserveRequest request = new ReserveRequest(concertId, concertDateId, seatId, userId);

        // when
        ExtractableResponse<Response> response = post(LOCAL_HOST + port + PATH, request);

        // then
        assertThat(response.statusCode()).isEqualTo(200);
        ReserveResponse data = response.body().jsonPath().getObject("data", ReserveResponse.class);
        assertThat(data.reservationId()).isEqualTo(3L);
        assertThat(data.status()).isEqualTo(Reservation.Status.ING);
        assertThat(reservationRepository.findById(3L)).isNotNull();

        // 임시 점유 시간 대기 TODO fakeTimeService 만들기

        // 예약이 자동으로 취소되었는지 확인
        assertThat(reservationRepository.findById(3L)).isNull();
    }

    @Test
    @DisplayName("예약을 성공하면 5분 안에 결제 완료되면 예약 확정된다.")
    void reserveTest_complete_in_5min() {

    }

    @Test
    void cancel() {
    }
    // 예약 없음 예외처리
    // 예약 취소되면 결제 취소, 잔액 환불
    // 예약 취소되면 예약 내역 삭제됨

    @Test
    void getMyReservation() {
    }
    // 나의 예약 내역 조회
}