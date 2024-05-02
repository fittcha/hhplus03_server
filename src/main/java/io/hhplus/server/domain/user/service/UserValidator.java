package io.hhplus.server.domain.user.service;

import io.hhplus.server.base.exception.CustomException;
import io.hhplus.server.domain.concert.ConcertExceptionEnum;
import io.hhplus.server.domain.concert.entity.ConcertDate;
import io.hhplus.server.domain.user.UserExceptionEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.logging.LogLevel;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserValidator {

    public void insufficientBalance(BigDecimal nowBalance, BigDecimal useBalance) {
        if (useBalance.compareTo(nowBalance) > 0) {
            throw new CustomException(UserExceptionEnum.INSUFFICIENT_BALANCE, null, LogLevel.INFO);
        }
    }
}
