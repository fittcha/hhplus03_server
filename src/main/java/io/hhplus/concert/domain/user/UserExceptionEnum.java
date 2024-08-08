package io.hhplus.concert.domain.user;

import io.hhplus.concert.base.enums.MessageCommInterface;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserExceptionEnum implements MessageCommInterface {

    INSUFFICIENT_BALANCE("USER.INSUFFICIENT_BALANCE", "잔액이 부족하여 사용 불가합니다."),
    ;

    private final String code;
    private final String message;
}
