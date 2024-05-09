package io.hhplus.server.controller.waiting;

import io.hhplus.server.base.exception.ApiResult;
import io.hhplus.server.controller.waiting.dto.request.CheckWaitingRequest;
import io.hhplus.server.controller.waiting.dto.response.CheckWaitingResponse;
import io.hhplus.server.domain.waiting.service.WaitingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "대기열", description = "waiting-controller")
@RequestMapping("/waits")
@RestController
@RequiredArgsConstructor
public class WaitingController {

    private final WaitingService service;

    @Operation(summary = "대기열 활성여부 조회", description = "토큰 요청값이 없으면 새로 발급하여 응답 반환, isActive 반환값에 따라 페이지 진입 가능")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = CheckWaitingResponse.class)))
    @GetMapping(value = "/token")
    public ApiResult<CheckWaitingResponse> checkWaiting(@RequestParam CheckWaitingRequest request) {
        return ApiResult.success(service.checkWaiting(request.userId(), request.token()));
    }
}
