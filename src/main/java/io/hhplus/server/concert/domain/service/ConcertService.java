package io.hhplus.server.concert.domain.service;

import io.hhplus.server.concert.controller.dto.response.GetConcertsResponse;
import io.hhplus.server.concert.controller.dto.response.GetConcertResponse;
import io.hhplus.server.concert.controller.dto.response.GetDatesResponse;
import io.hhplus.server.concert.controller.dto.response.GetSeatsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConcertService implements ConcertInterface {

    @Override
    public List<GetConcertsResponse> getConcerts() {
        return null;
    }

    @Override
    public GetConcertResponse getConcert() {
        return null;
    }

    @Override
    public List<GetDatesResponse> getDates() {
        return null;
    }

    @Override
    public GetSeatsResponse getSeats() {
        return null;
    }
}
