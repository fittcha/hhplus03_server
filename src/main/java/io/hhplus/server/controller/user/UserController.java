package io.hhplus.server.controller.user;

import io.hhplus.server.base.exception.ApiResult;
import io.hhplus.server.controller.user.dto.request.ChargeRequest;
import io.hhplus.server.controller.user.dto.response.GetBalanceResponse;
import io.hhplus.server.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "유저", description = "user-controller")
@RequestMapping("/users")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @Operation(summary = "잔액 조회")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = GetBalanceResponse.class)))
    @GetMapping("/{userId}/balance")
    public ApiResult<GetBalanceResponse> getBalance(@PathVariable(value = "userId") @NotNull Long userId) {
        return ApiResult.success(service.getBalance(userId));
    }

    @Operation(summary = "잔액 충전")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = GetBalanceResponse.class)))
    @PatchMapping("/{userId}/charge")
    public ApiResult<GetBalanceResponse> charge(@PathVariable(value = "userId") @NotNull Long userId,
                                                @RequestBody @Valid ChargeRequest request) {
        return ApiResult.success(service.charge(userId, request));
    }
}
