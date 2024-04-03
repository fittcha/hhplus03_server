package io.hhplus.server.concert.domain.service;

import io.hhplus.server.concert.controller.dto.response.GetConcertsResponse;
import io.hhplus.server.concert.controller.dto.response.GetConcertResponse;
import io.hhplus.server.concert.controller.dto.response.GetDatesResponse;
import io.hhplus.server.concert.controller.dto.response.GetSeatsResponse;

import java.util.List;

public interface ConcertInterface {

    // 콘서트 목록 조회
    List<GetConcertsResponse> getConcerts();

    // 콘서트 상세 조회
    GetConcertResponse getConcert();

    // 예약 가능 날짜 조회
    List<GetDatesResponse> getDates();

    // 좌석 조회
    GetSeatsResponse getSeats();
}
