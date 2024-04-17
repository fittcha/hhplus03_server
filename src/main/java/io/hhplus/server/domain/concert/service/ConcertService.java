package io.hhplus.server.domain.concert.service;

import io.hhplus.server.controller.concert.dto.response.GetConcertResponse;
import io.hhplus.server.controller.concert.dto.response.GetConcertsResponse;
import io.hhplus.server.controller.concert.dto.response.GetDatesResponse;
import io.hhplus.server.controller.concert.dto.response.GetSeatsResponse;
import io.hhplus.server.domain.concert.entity.Concert;
import io.hhplus.server.domain.concert.entity.Place;
import io.hhplus.server.domain.concert.entity.Seat;
import io.hhplus.server.domain.concert.repository.ConcertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConcertService implements ConcertInterface {

    private final ConcertRepository concertRepository;
    private final ConcertValidator concertValidator;
    private final ConcertReader concertReader;
    private final ConcertPlaceManager placeManager;
    private final ConcertReservationManager reservationManager;

    @Override
    public List<GetConcertsResponse> getConcerts() {
        List<Concert> concerts = concertRepository.findAll();
        return concerts.stream().map(GetConcertsResponse::from).toList();
    }

    @Override
    public GetConcertResponse getConcert(Long concertId) {
        Concert concert = concertRepository.findById(concertId);
        Place place = concertReader.findPlace(concert.getPlaceId());
        return GetConcertResponse.from(concert, place);
    }

    @Override
    public GetDatesResponse getDates(Long concertId) {
        Concert concert = concertRepository.findById(concertId);
        // validator
        concertValidator.dateIsNull(concert.getConcertDateList());

        return GetDatesResponse.from(concert.getConcertDateList());
    }

    @Override
    public GetSeatsResponse getSeats(Long concertId, Long concertDateId) {
        List<Seat> allSeats = placeManager.getSeatsByConcertId(concertId);
        List<Long> reservedSeatIds = reservationManager.getReservedSeatIdsByConcertDate(concertDateId);

        return GetSeatsResponse.from(allSeats, reservedSeatIds);
    }
}
