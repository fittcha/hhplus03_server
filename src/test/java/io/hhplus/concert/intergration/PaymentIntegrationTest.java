package io.hhplus.concert.intergration;

import io.hhplus.concert.base.exception.CustomException;
import io.hhplus.concert.controller.payment.dto.request.PayRequest;
import io.hhplus.concert.controller.payment.dto.response.PayResponse;
import io.hhplus.concert.domain.payment.PaymentExceptionEnum;
import io.hhplus.concert.domain.payment.entity.Payment;
import io.hhplus.concert.domain.user.UserExceptionEnum;
import io.hhplus.concert.intergration.base.BaseIntegrationTest;
import io.hhplus.concert.intergration.base.TestDataHandler;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.logging.LogLevel;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class PaymentIntegrationTest extends BaseIntegrationTest {

    @Autowired
    TestDataHandler testDataHandler;

    private static final String PATH = "/payments";


    @Test
    @DisplayName("결제 불가능한 상태면 결제를 실패한다.")
    void payTest_NOT_AVAILABLE_PAY() {
        // given
        testDataHandler.createPayment(Payment.Status.COMPLETE);
        long paymentId = 1L;
        PayRequest request = new PayRequest(1L);

        // when
        post(LOCAL_HOST + port + PATH + "/" + paymentId, request);

        // then
        assertThatThrownBy(() -> {
            throw new CustomException(PaymentExceptionEnum.NOT_AVAILABLE_PAY, null, LogLevel.INFO);
        })
                .isInstanceOf(CustomException.class)
                .hasMessageContaining("결제 가능한 상태가 아닙니다.");
    }

    @Test
    @DisplayName("사용자의 잔액이 결제금액보다 적으면 결제를 실패한다.")
    void payTest_INSUFFICIENT_BALANCE() {
        // given
        testDataHandler.createPayment(Payment.Status.READY);
        testDataHandler.settingUser(BigDecimal.valueOf(10000));
        long paymentId = 1L;
        PayRequest request = new PayRequest(1L);

        // when
        post(LOCAL_HOST + port + PATH + "/" + paymentId, request);

        // then
        assertThatThrownBy(() -> {
            throw new CustomException(UserExceptionEnum.INSUFFICIENT_BALANCE, null, LogLevel.INFO);
        })
                .isInstanceOf(CustomException.class)
                .hasMessageContaining("잔액이 부족하여 사용 불가합니다.");
    }

    @Test
    @DisplayName("결제를 성공하면 잔액이 차감되고 결제 완료 처리된다.")
    void payTest_success() {
        // given
        testDataHandler.createPayment(Payment.Status.READY);
        testDataHandler.settingUser(BigDecimal.valueOf(100000));
        long paymentId = 1L;
        PayRequest request = new PayRequest(1L);

        // when
        ExtractableResponse<Response> response = post(LOCAL_HOST + port + PATH + "/" + paymentId, request);

        // then
        assertThat(response.statusCode()).isEqualTo(200);
        PayResponse data = response.body().jsonPath().getObject("data", PayResponse.class);
        assertThat(data.isSuccess()).isTrue();
        assertThat(data.status()).isEqualTo(Payment.Status.COMPLETE);
        assertThat(data.balance()).isEqualByComparingTo(BigDecimal.valueOf(11000));
    }
}