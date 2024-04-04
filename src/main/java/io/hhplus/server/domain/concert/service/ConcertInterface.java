package io.hhplus.server.domain.concert.service;

import io.hhplus.server.controller.concert.dto.response.GetConcertsResponse;
import io.hhplus.server.controller.concert.dto.response.GetConcertResponse;
import io.hhplus.server.controller.concert.dto.response.GetDatesResponse;
import io.hhplus.server.controller.concert.dto.response.GetSeatsResponse;

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
