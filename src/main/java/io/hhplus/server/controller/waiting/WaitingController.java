package io.hhplus.server.controller.waiting;

import io.hhplus.server.base.exception.ApiResult;
import io.hhplus.server.controller.waiting.dto.request.AddWaitingQueueRequest;
import io.hhplus.server.controller.waiting.dto.request.IssueTokenRequest;
import io.hhplus.server.controller.waiting.dto.response.CheckActiveResponse;
import io.hhplus.server.controller.waiting.dto.response.IssueTokenResponse;
import io.hhplus.server.domain.waiting.service.WaitingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "대기열", description = "waiting-controller")
@RequestMapping("/waits")
@RestController
@RequiredArgsConstructor
public class WaitingController {

    private final WaitingService service;

    @Operation(summary = "토큰 발급")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = IssueTokenResponse.class)))
    @PostMapping(value = "/issue-token")
    public ApiResult<IssueTokenResponse> issueToken(@RequestBody @Valid IssueTokenRequest request) {
        return ApiResult.success(service.issueToken(request.userId()));
    }

    @Operation(summary = "대기열 저장", description = "첫 진입 시 또는 새로고침 시 호출")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = CheckActiveResponse.class)))
    @PostMapping(value = "/queue")
    public ApiResult<CheckActiveResponse> addWaitingQueue(@RequestBody @Valid AddWaitingQueueRequest request) {
        return ApiResult.success(service.addWaitingQueue(request.userId(), request.token()));
    }

    @Operation(summary = "대기열 확인", description = "대기 시 호출 (polling 방식)")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = CheckActiveResponse.class)))
    @PostMapping(value = "/check")
    public ApiResult<CheckActiveResponse> checkActive(@RequestBody @Valid AddWaitingQueueRequest request) {
        return ApiResult.success(service.checkActive(request.userId(), request.token()));
    }
}
