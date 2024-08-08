package io.hhplus.concert.domain.concert.service;

import io.hhplus.concert.controller.concert.dto.response.GetConcertResponse;
import io.hhplus.concert.controller.concert.dto.response.GetConcertsResponse;
import io.hhplus.concert.controller.concert.dto.response.GetDatesResponse;
import io.hhplus.concert.controller.concert.dto.response.GetSeatsResponse;
import io.hhplus.concert.domain.concert.entity.Seat;

import java.util.List;

public interface ConcertInterface {

    /* 콘서트 목록 조회 */
    List<GetConcertsResponse> getConcerts();

    /* 콘서트 상세 조회 */
    GetConcertResponse getConcert(Long concertId);

    /* 날짜 목록 조회 */
    GetDatesResponse getDates(Long concertId);

    /* 예약 가능 좌석 조회 */
    GetSeatsResponse getAvailableSeats(Long concertDateId);

    /* 좌석 상태 변경 */
    void patchSeatStatus(Long concertDateId, int seatNum, Seat.Status status);
}
