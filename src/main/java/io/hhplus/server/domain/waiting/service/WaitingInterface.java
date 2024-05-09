package io.hhplus.server.domain.waiting.service;

import io.hhplus.server.controller.waiting.dto.response.CheckWaitingResponse;

public interface WaitingInterface {

    /* token 발급 */
    String issueToken(Long userId);

    /*
     * 대기열 확인 (활성: 진입 / 비활성: 대기 정보 반환)
     */
    CheckWaitingResponse checkWaiting(Long userId, String token);
}
