package io.hhplus.server.domain.unit.service;

import io.hhplus.server.controller.user.dto.request.ChargeRequest;
import io.hhplus.server.controller.user.dto.response.GetBalanceResponse;
import io.hhplus.server.domain.user.entity.User;
import io.hhplus.server.domain.user.repository.UserRepository;
import io.hhplus.server.domain.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.when;

class UserServiceTest {

    private UserService userService;
    private UserRepository userRepository;

    private User 사용자;

    @BeforeEach
    void setUp() {
        // mocking
        userRepository = Mockito.mock(UserRepository.class);

        userService = new UserService(
                userRepository
        );

        // 사용자 정보 세팅
        사용자 = new User(1L, BigDecimal.valueOf(100000));
    }

    @Test
    @DisplayName("잔액을 조회한다.")
    void getBalanceTest_balance() {
        // given
        Long userId = 1L;

        // when
        when(userRepository.findById(userId)).thenReturn(사용자);
        GetBalanceResponse response = userService.getBalance(userId);

        // then
        assertThat(response.balance()).isEqualTo(BigDecimal.valueOf(100000));
    }

    @Test
    @DisplayName("잔액을 충전한다.")
    void chargeTest_charge() {
        // given
        Long userId = 1L;
        ChargeRequest request = new ChargeRequest(10000);

        // when
        when(userRepository.findById(userId)).thenReturn(사용자);
        GetBalanceResponse response = userService.charge(userId, request);

        // then
        assertThat(response.balance()).isEqualTo(BigDecimal.valueOf(110000));
    }
}