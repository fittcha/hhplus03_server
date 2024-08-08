package io.hhplus.concert.domain.payment.service;

import io.hhplus.concert.base.exception.CustomException;
import io.hhplus.concert.domain.payment.PaymentExceptionEnum;
import io.hhplus.concert.domain.payment.entity.Payment;
import io.hhplus.concert.domain.user.UserExceptionEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.logging.LogLevel;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class PaymentValidator {

    public void checkBalance(BigDecimal paymentPrice, BigDecimal balance) {
        if (balance.compareTo(paymentPrice) < 0) {
            throw new CustomException(UserExceptionEnum.INSUFFICIENT_BALANCE, null, LogLevel.INFO);
        }
    }

    public void checkPayStatus(Payment.Status status) {
        if (!status.equals(Payment.Status.READY)) {
            throw new CustomException(PaymentExceptionEnum.NOT_AVAILABLE_PAY, null, LogLevel.INFO);
        }
    }

    public void checkCancelStatus(Payment.Status status) {
        if (!(status.equals(Payment.Status.READY) || status.equals(Payment.Status.COMPLETE))) {
            throw new CustomException(PaymentExceptionEnum.NOT_AVAILABLE_CANCEL, null, LogLevel.INFO);
        }
    }
}
