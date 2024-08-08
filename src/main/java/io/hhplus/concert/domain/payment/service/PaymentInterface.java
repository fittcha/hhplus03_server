package io.hhplus.concert.domain.payment.service;

import io.hhplus.concert.controller.payment.dto.request.CreateRequest;
import io.hhplus.concert.controller.payment.dto.request.PayRequest;
import io.hhplus.concert.controller.payment.dto.response.CreateResponse;
import io.hhplus.concert.controller.payment.dto.response.PayResponse;
import io.hhplus.concert.domain.payment.service.dto.CancelPaymentResultResDto;

public interface PaymentInterface {

    /* 결제 요청 */
    PayResponse pay(Long paymentId, PayRequest request);

    /* 결제 정보 생성 */
    CreateResponse create(CreateRequest request);

    /* 결제 취소 */
    CancelPaymentResultResDto cancel(Long paymentId);
}
