package io.hhplus.concert.domain.concert.service;

import io.hhplus.concert.base.exception.CustomException;
import io.hhplus.concert.domain.concert.ConcertExceptionEnum;
import io.hhplus.concert.domain.concert.entity.ConcertDate;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.logging.LogLevel;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ConcertValidator {

    public void dateIsNull(List<ConcertDate> concertDateList) {
        if (concertDateList.isEmpty()) {
            throw new CustomException(ConcertExceptionEnum.DATE_IS_NULL, null, LogLevel.INFO);
        }
    }
}
