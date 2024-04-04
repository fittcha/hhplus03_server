package io.hhplus.server.domain.payment.service;

import io.hhplus.server.controller.payment.dto.request.PayRequest;
import io.hhplus.server.controller.payment.dto.response.PayResponse;
import io.hhplus.server.domain.payment.PaymentEnums;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService implements PaymentInterface {

    @Override
    public PayResponse pay(Long paymentId, PayRequest request) {
        // dummy data
        return PayResponse.builder()
                .paymentId(1L)
                .status(PaymentEnums.Status.COMPLETE)
                .paymentPrice(79000)
                .balance(1000)
                .build();
    }
}
