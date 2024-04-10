package io.hhplus.server.domain.concert.service;

import io.hhplus.server.CustomException;
import io.hhplus.server.domain.concert.ConcertExceptionEnum;
import io.hhplus.server.domain.concert.entity.ConcertDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ConcertValidator {

    public void dateIsNull(List<ConcertDate> concertDateList) {
        if (concertDateList.isEmpty()) {
            throw new CustomException(ConcertExceptionEnum.DATE_IS_NULL);
        }
    }
}
