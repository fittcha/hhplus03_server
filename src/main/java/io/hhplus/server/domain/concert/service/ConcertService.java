package io.hhplus.server.domain.concert.service;

import io.hhplus.server.controller.concert.dto.response.GetConcertsResponse;
import io.hhplus.server.controller.concert.dto.response.GetConcertResponse;
import io.hhplus.server.controller.concert.dto.response.GetDatesResponse;
import io.hhplus.server.controller.concert.dto.response.GetSeatsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConcertService implements ConcertInterface {

    @Override
    public List<GetConcertsResponse> getConcerts() {
        // dummy data
        return List.of(
                new GetConcertsResponse(1L, "2024 테스트 콘서트 in 서울", ZonedDateTime.now().minusDays(1)),
                new GetConcertsResponse(2L, "2024 더미 콘서트 in 부산", ZonedDateTime.now().minusDays(3))
        );
    }

    @Override
    public GetConcertResponse getConcert(Long concertId) {
        // dummy data
        return GetConcertResponse.builder()
                .concertId(1L)
                .name("2024 테스트 콘서트 in 서울")
                .hall("서울 장충체육관")
                .period("2024.05.05~2024.05.25")
                .price("79,000원~119,000원")
                .createdAt(ZonedDateTime.now().minusDays(1))
                .build();
    }

    @Override
    public List<GetDatesResponse> getDates(Long concertId) {
        // dummy data
        return List.of(
                new GetDatesResponse(1L, ZonedDateTime.now().plusMonths(1), false),
                new GetDatesResponse(2L, ZonedDateTime.now().plusMonths(2), true)
        );
    }

    @Override
    public List<GetSeatsResponse> getSeats(Long concertId, Long concertDateId) {
        // dummy data
        return List.of(
                new GetSeatsResponse(1L, 1, false),
                new GetSeatsResponse(2L, 2, false),
                new GetSeatsResponse(3L, 3, true),
                new GetSeatsResponse(4L, 4, true),
                new GetSeatsResponse(5L, 5, false)
        );
    }
}
