package io.hhplus.server.intergration;

import io.hhplus.server.controller.user.dto.request.ChargeRequest;
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
}