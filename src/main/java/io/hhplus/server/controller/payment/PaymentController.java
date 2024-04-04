package io.hhplus.server.controller.payment;

import io.hhplus.server.controller.payment.dto.request.PayRequest;
import io.hhplus.server.controller.payment.dto.response.PayResponse;
import io.hhplus.server.domain.payment.service.PaymentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/payments")
@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService service;

    @PostMapping("/{paymentId}")
    public PayResponse pay(@PathVariable(value = "paymentId") @NotNull Long paymentId,
                           @RequestBody @Valid PayRequest request) {
        return service.pay(paymentId, request);
    }
}
