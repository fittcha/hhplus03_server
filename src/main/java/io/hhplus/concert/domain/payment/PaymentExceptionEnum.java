package io.hhplus.concert.domain.payment;

import io.hhplus.concert.base.enums.MessageCommInterface;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentExceptionEnum implements MessageCommInterface {

    NOT_AVAILABLE_PAY("PAYMENT.NOT_AVAILABLE_PAY", "결제 가능한 상태가 아닙니다."),
    NOT_AVAILABLE_CANCEL("PAYMENT.NOT_AVAILABLE_CANCEL", "취소 가능한 상태가 아닙니다."),
    ;

    private final String code;
    private final String message;
}
