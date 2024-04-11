package io.hhplus.server.domain.waiting.service;

import io.hhplus.server.controller.waiting.dto.response.CheckActiveResponse;
import io.hhplus.server.controller.waiting.dto.response.IssueTokenResponse;

public interface WaitingInterface {

    /* token 발급 */
    IssueTokenResponse issueToken(Long userId);

    /*
     * 대기열 저장
     *
     * 첫 진입 시 또는 새로고침 시 호출
     */
    CheckActiveResponse addWaitingQueue(Long userId, String token);

    /*
     * 대기열 확인 (활성: 진입 / 비활성: 대기 정보 반환)
     *
     * 대기 시 호출 (polling 방식)
     */
    CheckActiveResponse checkActive(Long userId, String token);
}
