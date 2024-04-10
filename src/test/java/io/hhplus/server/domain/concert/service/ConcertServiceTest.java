package io.hhplus.server.domain.concert.service;

import io.hhplus.server.domain.concert.entity.Concert;
import io.hhplus.server.domain.concert.repository.ConcertRepository;
import io.hhplus.server.domain.place.service.PlaceService;
import io.hhplus.server.domain.reservation.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ConcertServiceTest {

    private ConcertService concertService;
    private ConcertRepository concertRepository;
    private ConcertValidator concertValidator;
    private PlaceService placeService;
    private ReservationService reservationService;

    private Concert 임영웅_콘서트;

    @BeforeEach
    void setUp() {
        // mocking
        concertRepository = Mockito.mock(ConcertRepository.class);
        concertValidator = Mockito.mock(ConcertValidator.class);
        placeService = Mockito.mock(PlaceService.class);
        reservationService = Mockito.mock(ReservationService.class);

        concertService = new ConcertService(
                concertRepository,
                concertValidator,
                placeService,
                reservationService
        );

        // 임영웅 콘서트 정보 세팅
        임영웅_콘서트 = Concert.builder()
                .name("2024 임영웅 콘서트 [IM HERO - THE STADIUM]")
                .placeId(1L)
                .build();
    }

    @Test
    @DisplayName("콘서트_전체_목록_조회")
    void getConcertsTest_콘서트_전체_목록_조회() {
        // given

        // when

        // then
    }

    @Test
    void getConcert() {
    }

    @Test
    void getDates() {
    }

    @Test
    void getSeats() {
    }
}