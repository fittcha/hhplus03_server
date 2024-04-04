package io.hhplus.server.controller.user;

import io.hhplus.server.controller.user.dto.request.ChargeRequest;
import io.hhplus.server.controller.user.dto.response.GetBalanceResponse;
import io.hhplus.server.controller.user.dto.response.GetMyReservationsResponse;
import io.hhplus.server.domain.user.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/users")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping("/{userId}/balance")
    public GetBalanceResponse getBalance(@PathVariable(value = "userId") @NotNull Long userId) {
        return service.getBalance(userId);
    }

    @PatchMapping("/{userId}/charge")
    public void charge(@PathVariable(value = "userId") @NotNull Long userId,
                       @RequestBody @Valid ChargeRequest request) {
        service.charge(userId, request);
    }

    @GetMapping("/{userId}/reservation")
    public List<GetMyReservationsResponse> getMyReservation(@PathVariable(value = "userId") @NotNull Long userId) {
        return service.getMyReservations(userId);
    }
}
