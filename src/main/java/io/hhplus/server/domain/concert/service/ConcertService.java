package io.hhplus.server.domain.concert.service;

import io.hhplus.server.controller.concert.dto.response.GetConcertResponse;
import io.hhplus.server.controller.concert.dto.response.GetConcertsResponse;
import io.hhplus.server.controller.concert.dto.response.GetDatesResponse;
import io.hhplus.server.controller.concert.dto.response.GetSeatsResponse;
import io.hhplus.server.domain.concert.dto.GetSeatsQueryResDto;
import io.hhplus.server.domain.concert.entity.Concert;
import io.hhplus.server.domain.concert.entity.Place;
import io.hhplus.server.domain.concert.repository.ConcertRepository;
import io.hhplus.server.domain.concert.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConcertService implements ConcertInterface {

    private final ConcertRepository concertRepository;
    private final PlaceRepository placeRepository;

    @Override
    public List<GetConcertsResponse> getConcerts() {
        List<Concert> concerts = concertRepository.findAll();
        return concerts.stream().map(GetConcertsResponse::from).toList();
    }

    @Override
    public GetConcertResponse getConcert(Long concertId) {
        Concert concert = concertRepository.findById(concertId);
        Place place = placeRepository.findById(concert.getPlaceId());
        return GetConcertResponse.from(concert, place);
    }

    @Override
    public List<GetDatesResponse> getDates(Long concertId) {
        Concert concert = concertRepository.findById(concertId);
        if (concert.getConcertDateList().isEmpty()) {
//            return "예정된 콘서트 날짜가 없습니다.";
            return new ArrayList<>();
        }

        return concert.getConcertDateList().stream().map(GetDatesResponse::from).toList();
    }

    @Override
    public List<GetSeatsResponse> getSeats(Long concertId, Long concertDateId) {
        List<GetSeatsQueryResDto> resDtos = concertRepository.getSeatsByConcertDate(concertId, concertDateId);
        return null;
    }
}
