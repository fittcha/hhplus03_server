package io.hhplus.server.controller.waiting.dto.response;

public record CheckActiveResponse(
        // 활성 상태 여부
        boolean isActive,
        // 대기 상태일 경우 대기 정보 반환
        WaitingTicketInfo waitingTicketInfo
) {

    public record WaitingTicketInfo(
            Long waitingNum,
            Long expectedWaitTimeInSeconds
    ) {
    }
}
