package io.hhplus.server.domain.payment.service;

import io.hhplus.server.CustomException;
import io.hhplus.server.domain.payment.PaymentEnums;
import io.hhplus.server.domain.payment.PaymentExceptionEnum;
import io.hhplus.server.domain.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class PaymentValidator {

    private final PaymentRepository paymentRepository;

    public void checkBalance(BigDecimal paymentPrice, BigDecimal balance) {
        if (balance.compareTo(paymentPrice) < 0) {
            throw new CustomException(PaymentExceptionEnum.INSUFFICIENT_BALANCE);
        }
    }

    public void checkStatus(PaymentEnums.Status status) {
        if (!status.equals(PaymentEnums.Status.READY)) {
            throw new CustomException(PaymentExceptionEnum.NOT_AVAILABLE_STATUS);
        }
    }
}
