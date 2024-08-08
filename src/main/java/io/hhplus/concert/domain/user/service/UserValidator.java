package io.hhplus.concert.domain.user.service;

import io.hhplus.concert.base.exception.CustomException;
import io.hhplus.concert.domain.user.UserExceptionEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.logging.LogLevel;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class UserValidator {

    public void insufficientBalance(BigDecimal nowBalance, BigDecimal useBalance) {
        if (useBalance.compareTo(nowBalance) > 0) {
            throw new CustomException(UserExceptionEnum.INSUFFICIENT_BALANCE, null, LogLevel.INFO);
        }
    }
}
