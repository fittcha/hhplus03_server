package io.hhplus.server.domain.payment.service;

import io.hhplus.server.controller.payment.dto.request.PayRequest;
import io.hhplus.server.controller.payment.dto.response.PayResponse;

public interface PaymentInterface {

    // 결제 요청
    PayResponse pay(Long paymentId, PayRequest request);
}
