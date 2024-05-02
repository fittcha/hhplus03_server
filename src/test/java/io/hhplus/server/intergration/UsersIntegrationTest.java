package io.hhplus.server.intergration;

import io.hhplus.server.controller.user.dto.request.ChargeRequest;
import io.hhplus.server.controller.user.dto.request.UseRequest;
import io.hhplus.server.controller.user.dto.response.GetBalanceResponse;
import io.hhplus.server.domain.user.repository.UserRepository;
import io.hhplus.server.intergration.base.BaseIntegrationTest;
import io.hhplus.server.intergration.base.TestDataHandler;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

class UsersIntegrationTest extends BaseIntegrationTest {

    @Autowired
    TestDataHandler testDataHandler;
    @Autowired
    UserRepository userRepository;

    private static final String PATH = "/users";

    @Test
    @DisplayName("잔액을 조회하면, 해당 사용자의 잔액을 반환한다.")
    void getBalanceTest() {
        // given
        long userId = 1L;
        testDataHandler.settingUser(BigDecimal.valueOf(12000));

        // when
        ExtractableResponse<Response> response = get(LOCAL_HOST + port + PATH + "/" + userId + "/balance");

        // then
        assertThat(response.statusCode()).isEqualTo(200);
        GetBalanceResponse data = response.body().jsonPath().getObject("data", GetBalanceResponse.class);
        assertThat(data.balance()).isEqualByComparingTo(BigDecimal.valueOf(12000));
    }

    @Test
    @DisplayName("잔액을 충전하면, 해당 사용자의 충전 후 잔액을 반환한다.")
    void chargeTest() {
        // given
        long userId = 1L;
        testDataHandler.settingUser(BigDecimal.valueOf(12000));
        ChargeRequest request = new ChargeRequest(50000);

        // when
        ExtractableResponse<Response> response = patch(LOCAL_HOST + port + PATH + "/" + userId + "/charge", request);

        // then
        assertThat(response.statusCode()).isEqualTo(200);
        GetBalanceResponse data = response.body().jsonPath().getObject("data", GetBalanceResponse.class);
        assertThat(data.balance()).isEqualByComparingTo(BigDecimal.valueOf(62000));
    }

    // 동시성 테스트
    @Test
    @DisplayName("유저 잔액 충전 요청이 따닥 동시에 들어와도 잘 처리된다.")
    void chargeTest_따닥_lock() {
        // given
        testDataHandler.settingUser(BigDecimal.ZERO);

        // when - 동시에 충전 요청
        AtomicInteger successCount = new AtomicInteger(0);
        List<CompletableFuture<ExtractableResponse<Response>>> futures = IntStream.range(0, 3)
                .mapToObj(i -> CompletableFuture.supplyAsync(() -> {
                    ChargeRequest request = new ChargeRequest(3000);
                    return patch(LOCAL_HOST + port + PATH + "/1/charge/retry", request);
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
            }
        });
        assertThat(successCount.get()).isEqualTo(3);
    }

    @Test
    @DisplayName("유저 잔액 충전-사용 요청이 따닥 동시에 들어와도 잘 처리된다.")
    void chargeAndUseTest_lock() {
        // given
        testDataHandler.settingUser(BigDecimal.ZERO);

        // when - 동시에 충전-사용 요청
        AtomicInteger successCount = new AtomicInteger(0);
        List<CompletableFuture<ExtractableResponse<Response>>> futures = IntStream.range(0, 2)
                .mapToObj(i -> CompletableFuture.supplyAsync(() -> {
                    if (i % 2 == 0) {
                        ChargeRequest request = new ChargeRequest(3000);
                        return patch(LOCAL_HOST + port + PATH + "/1/charge/retry", request);
                    } else {
                        UseRequest request = new UseRequest(2000);
                        return patch(LOCAL_HOST + port + PATH + "/1/use/retry", request);
                    }
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
            }
        });
        assertThat(successCount.get()).isEqualTo(2);
    }

    @Test
    @DisplayName("유저 잔액 사용 요청이 동시에 들어와도 잔액이 잘 차감되며 잔액 부족 시 실패한다.")
    void useTest_use_before_lack_lock() {
        // given - 잔액 10000원
        testDataHandler.settingUser(BigDecimal.valueOf(10000));

        // when - 동시에 사용 요청 3000원 * 5번
        AtomicInteger successCount = new AtomicInteger(0);
        List<CompletableFuture<ExtractableResponse<Response>>> futures = IntStream.range(0, 5)
                .mapToObj(i -> CompletableFuture.supplyAsync(() -> {
                    UseRequest request = new UseRequest(3000);
                    return patch(LOCAL_HOST + port + PATH + "/1/use/retry", request);
                }))
                .toList();
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        // then - 3번만 성공
        List<ExtractableResponse<Response>> responses = futures.stream()
                .map(CompletableFuture::join)
                .toList();
        responses.forEach(response -> {
            if (response.body().jsonPath().getObject("success", Boolean.class).equals(true)) {
                successCount.getAndIncrement();
            }
        });
        assertThat(successCount.get()).isEqualTo(3);
    }
}