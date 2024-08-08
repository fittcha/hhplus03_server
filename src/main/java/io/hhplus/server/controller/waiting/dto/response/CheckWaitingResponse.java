package io.hhplus.server.controller.waiting.dto.response;

public record CheckWaitingResponse(
        String token,
        boolean isActive,
        // 대기 상태일 경우 대기 정보 반환
        WaitingTicketInfo waitingTicketInfo
) {

    public record WaitingTicketInfo(
            Long waitingNum,
            Long waitTimeInSeconds
    ) {
    }
}
