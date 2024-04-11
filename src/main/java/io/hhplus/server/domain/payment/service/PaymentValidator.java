package io.hhplus.server.domain.payment.service;

import io.hhplus.server.base.exception.CustomException;
import io.hhplus.server.domain.payment.PaymentEnums;
import io.hhplus.server.domain.payment.PaymentExceptionEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class PaymentValidator {

    public void checkBalance(BigDecimal paymentPrice, BigDecimal balance) {
        if (balance.compareTo(paymentPrice) < 0) {
            throw new CustomException(PaymentExceptionEnum.INSUFFICIENT_BALANCE);
        }
    }

    public void checkPayStatus(PaymentEnums.Status status) {
        if (!status.equals(PaymentEnums.Status.READY)) {
            throw new CustomException(PaymentExceptionEnum.NOT_AVAILABLE_PAY);
        }
    }

    public void checkCancelStatus(PaymentEnums.Status status) {
        if (!(status.equals(PaymentEnums.Status.READY) || status.equals(PaymentEnums.Status.COMPLETE))) {
            throw new CustomException(PaymentExceptionEnum.NOT_AVAILABLE_CANCEL);
        }
    }
}
