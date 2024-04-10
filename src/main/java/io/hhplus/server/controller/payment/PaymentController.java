package io.hhplus.server.controller.payment;

import io.hhplus.server.controller.payment.dto.request.PayRequest;
import io.hhplus.server.controller.payment.dto.response.PayResponse;
import io.hhplus.server.domain.payment.PaymentEnums;
import io.hhplus.server.domain.payment.service.PaymentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RequestMapping("/payments")
@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService service;

    @PostMapping("/{paymentId}")
    public PayResponse pay(@PathVariable(value = "paymentId") @NotNull Long paymentId,
                           @RequestBody @Valid PayRequest request) {
        // dummy data
        return PayResponse.builder()
                .paymentId(1L)
                .status(PaymentEnums.Status.COMPLETE)
                .paymentPrice(BigDecimal.valueOf(79000))
                .balance(BigDecimal.valueOf(1000))
                .build();
    }
}
