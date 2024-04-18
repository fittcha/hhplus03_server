package io.hhplus.server.domain.unit.service;

import io.hhplus.server.base.jwt.JwtService;
import io.hhplus.server.controller.waiting.dto.response.CheckActiveResponse;
import io.hhplus.server.controller.waiting.dto.response.IssueTokenResponse;
import io.hhplus.server.domain.waiting.entity.WaitingQueue;
import io.hhplus.server.domain.waiting.repository.WaitingQueueRepository;
import io.hhplus.server.domain.waiting.service.WaitingService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.when;

@Slf4j
class WaitingServiceTest {

    private WaitingService waitingService;
    private WaitingQueueRepository waitingQueueRepository;
    private JwtService jwtService;

    private String 토큰;
    private WaitingQueue 활성유저;
    private WaitingQueue 대기유저;

    @BeforeEach
    void setUp() {
        // mocking
        waitingQueueRepository = Mockito.mock(WaitingQueueRepository.class);
        jwtService = Mockito.mock(JwtService.class);

        waitingService = new WaitingService(waitingQueueRepository, jwtService);

        // 세팅
        토큰 = "023ADO=fASDF234ji%fAKF=";
        활성유저 = WaitingQueue.toActiveEntity(1L, 토큰);
        대기유저 = WaitingQueue.toWaitingEntity(1L, 토큰);
    }

    @Test
    @DisplayName("토큰을 생성한다.")
    void issueTokenTest_create() {
        // given
        Long userId = 1L;

        // when
        when(jwtService.createToken(userId)).thenReturn(토큰);
        IssueTokenResponse response = waitingService.issueToken(userId);

        // then
        assertThat(response.token()).isEqualTo(토큰);
    }

    @Test
    @DisplayName("진입 가능하면 활성유저로 대기열에 추가한다.")
    void addWaitingQueueTest_active() {
        // given
        Long userId = 1L;
        String token = 토큰;

        // when
        when(waitingQueueRepository.findByUserId(userId)).thenReturn(null);
        when(waitingQueueRepository.countByStatusIs(any())).thenReturn(15L);
        CheckActiveResponse response = waitingService.addWaitingQueue(userId, token);

        // then
        assertThat(response.isActive()).isTrue();
    }

    @Test
    @DisplayName("진입 불가능할 시 대기유저로 대기열에 추가한다.")
    void addWaitingQueueTest_wait() {
        // given
        Long userId = 1L;
        String token = 토큰;

        // when
        when(waitingQueueRepository.findByUserId(userId)).thenReturn(null);
        when(waitingQueueRepository.countByStatusIs(WaitingQueue.Status.ACTIVE)).thenReturn(50L);
        when(waitingQueueRepository.countByStatusIs(WaitingQueue.Status.WAITING)).thenReturn(9L);
        CheckActiveResponse response = waitingService.addWaitingQueue(userId, token);

        // then
        assertThat(response.isActive()).isFalse();
        assertThat(response.waitingTicketInfo().waitingNum()).isEqualTo(9L);
        assertThat(response.waitingTicketInfo().expectedWaitTimeInSeconds()).isEqualTo(540);
    }

    @Test
    @DisplayName("대기열 상태를 확인하였을 때, 대기열 정보가 없거나 만료된 상태")
    void checkActiveTest_expired() {
        // given
        Long userId = 1L;
        String token = 토큰;

        // when
        when(waitingQueueRepository.findByUserIdAndToken(userId, token)).thenReturn(null);

        // then
        EntityNotFoundException expected = assertThrows(EntityNotFoundException.class, () ->
                waitingService.checkActive(userId, token));
        assertThat(expected.getMessage()).isEqualTo("새로고침하여 다시 진입하세요.");
    }

    @Test
    @DisplayName("대기열 상태를 확인하였을 때, 진입 가능")
    void checkActiveTest_active() {
        // given
        Long userId = 1L;
        String token = 토큰;

        // when
        when(waitingQueueRepository.findByUserIdAndToken(userId, token)).thenReturn(활성유저);
        CheckActiveResponse response = waitingService.checkActive(userId, token);

        // then
        assertThat(response.isActive()).isTrue();
    }

    @Test
    @DisplayName("대기열 상태를 확인하였을 때, 진입 불가")
    void checkActiveTest_wait() {
        // given
        Long userId = 1L;
        String token = 토큰;

        // when
        when(waitingQueueRepository.findByUserIdAndToken(userId, token)).thenReturn(대기유저);
        when(waitingQueueRepository.countByRequestTimeBeforeAndStatusIs(any(), any())).thenReturn(15L);
        CheckActiveResponse response = waitingService.checkActive(userId, token);

        // then
        assertThat(response.isActive()).isFalse();
        assertThat(response.waitingTicketInfo().waitingNum()).isEqualTo(15L);
    }
}