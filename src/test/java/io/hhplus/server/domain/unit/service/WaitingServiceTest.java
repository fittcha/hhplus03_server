package io.hhplus.server.domain.unit.service;

import io.hhplus.server.base.jwt.JwtService;
import io.hhplus.server.base.redis.service.RedisZSetService;
import io.hhplus.server.domain.waiting.service.WaitingService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

@Slf4j
class WaitingServiceTest {

    private WaitingService waitingService;
    private RedisZSetService redisZSetService;
    private JwtService jwtService;

    private String 토큰;

    @BeforeEach
    void setUp() {
        // mocking
        redisZSetService = Mockito.mock(RedisZSetService.class);
        jwtService = Mockito.mock(JwtService.class);

        waitingService = new WaitingService(redisZSetService, jwtService);

        // 세팅
        토큰 = "023ADO=fASDF234ji%fAKF=";
    }

    @Test
    @DisplayName("토큰을 생성한다.")
    void issueTokenTest_create() {
        // given
        Long userId = 1L;
    }

    @Test
    @DisplayName("진입 가능하면 활성유저로 대기열에 추가한다.")
    void addWaitingQueueTest_active() {
        // given
        Long userId = 1L;
        String token = 토큰;
    }

    @Test
    @DisplayName("진입 불가능할 시 대기유저로 대기열에 추가한다.")
    void addWaitingQueueTest_wait() {
        // given
        Long userId = 1L;
        String token = 토큰;
    }

    @Test
    @DisplayName("대기열 상태를 확인하였을 때, 대기열 정보가 없거나 만료된 상태")
    void checkWaitingTest_expired() {
        // given
        Long userId = 1L;
        String token = 토큰;
    }

    @Test
    @DisplayName("대기열 상태를 확인하였을 때, 진입 가능")
    void checkActiveTest_waiting() {
        // given
        Long userId = 1L;
        String token = 토큰;
    }

    @Test
    @DisplayName("대기열 상태를 확인하였을 때, 진입 불가")
    void checkWaitingTest_wait() {
        // given
        Long userId = 1L;
        String token = 토큰;
    }
}