package io.hhplus.server.domain.payment.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RefundPaymentEvent {

    private final Long paymentId;
}
