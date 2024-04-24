package io.hhplus.server.domain.unit.service;

import io.hhplus.server.base.exception.CustomException;
import io.hhplus.server.controller.concert.dto.response.GetConcertResponse;
import io.hhplus.server.controller.concert.dto.response.GetConcertsResponse;
import io.hhplus.server.controller.concert.dto.response.GetDatesResponse;
import io.hhplus.server.controller.concert.dto.response.GetSeatsResponse;
import io.hhplus.server.domain.concert.ConcertExceptionEnum;
import io.hhplus.server.domain.concert.entity.Concert;
import io.hhplus.server.domain.concert.entity.ConcertDate;
import io.hhplus.server.domain.concert.entity.Place;
import io.hhplus.server.domain.concert.entity.Seat;
import io.hhplus.server.domain.concert.repository.ConcertRepository;
import io.hhplus.server.domain.concert.service.ConcertReader;
import io.hhplus.server.domain.concert.service.ConcertService;
import io.hhplus.server.domain.concert.service.ConcertValidator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.logging.LogLevel;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.when;
import static org.mockito.Mockito.doThrow;

@Slf4j
class ConcertServiceTest {

    private ConcertService concertService;
    private ConcertRepository concertRepository;
    private ConcertValidator concertValidator;
    private ConcertReader concertReader;

    private Concert 임영웅_콘서트;
    private Place 상암_월드컵경기장;
    private ConcertDate 회차1;
    private ConcertDate 회차2;
    private List<Seat> 좌석;

    @BeforeEach
    void setUp() {
        // mocking
        concertRepository = Mockito.mock(ConcertRepository.class);
        concertValidator = Mockito.mock(ConcertValidator.class);

        concertService = new ConcertService(
                concertRepository,
                concertValidator
        );

        // 콘서트 정보 세팅
        상암_월드컵경기장 = Place.builder()
                .name("상암 월드컵경기장")
                .seatsCnt(5)
                .build();

        회차1 = new ConcertDate(1L,
                ZonedDateTime.of(
                        LocalDateTime.of(2024, 5, 25, 18, 30, 0),
                        ZoneId.of("Asia/Seoul")));
        회차2 = new ConcertDate(1L,
                ZonedDateTime.of(
                        LocalDateTime.of(2024, 5, 26, 19, 0, 0),
                        ZoneId.of("Asia/Seoul")));

        임영웅_콘서트 = Concert.builder()
                .name("2024 임영웅 콘서트 [IM HERO - THE STADIUM]")
                .place(상암_월드컵경기장)
                .concertDateList(List.of(회차1, 회차2))
                .build();
    }

    @Test
    @DisplayName("콘서트 전체 목록을 조회한다.")
    void getConcertsTest_all() {
        // when
        when(concertRepository.findAll()).thenReturn(List.of(임영웅_콘서트));
        List<GetConcertsResponse> responses = concertService.getConcerts();

        // then
        assertThat(responses.get(0).name()).isEqualTo("2024 임영웅 콘서트 [IM HERO - THE STADIUM]");
    }

    @Test
    @DisplayName("콘서트 상세정보를 조회한다.")
    void getConcertTest_detail() {
        // given
        Long concertId = 1L;

        // when
        when(concertRepository.findById(concertId)).thenReturn(임영웅_콘서트);
        GetConcertResponse response = concertService.getConcert(concertId);

        // then
        log.info(response.toString());
        assertThat(response.name()).isEqualTo("2024 임영웅 콘서트 [IM HERO - THE STADIUM]");
        assertThat(response.period()).isEqualTo("2024.05.25~2024.05.26");
        assertThat(response.place()).isEqualTo("상암 월드컵경기장");
    }

    @Test
    @DisplayName("예정된 날짜가 없으면 예외 처리")
    void getDatesTest_date_is_null() {
        // given
        Long concertId = 1L;

        // when
        when(concertRepository.findById(concertId)).thenReturn(Concert.builder()
                        .name("날짜없는 콘서트")
                        .place(상암_월드컵경기장)
                        .concertDateList(new ArrayList<>())
                .build());
        doThrow(new CustomException(ConcertExceptionEnum.DATE_IS_NULL, null, LogLevel.INFO)).when(concertValidator).dateIsNull(any());

        // then
        CustomException expected = assertThrows(CustomException.class, () ->
                concertValidator.dateIsNull(new ArrayList<>()));
        assertThat(expected.getMessage()).isEqualTo("예정된 콘서트 날짜가 없습니다.");
    }

    @Test
    @DisplayName("콘서트 회차 목록을 조회한다.")
    void getDatesTest_dates() {
        // given
        Long concertId = 1L;

        // when
        when(concertRepository.findById(concertId)).thenReturn(임영웅_콘서트);
        when(concertRepository.existByConcertDateAndStatus(anyLong(), any())).thenReturn(true);
        GetDatesResponse response = concertService.getDates(concertId);

        // then
        assertThat(response.dates().size()).isEqualTo(2);
        assertThat(response.dates().get(0).date()).isEqualTo(ZonedDateTime.of(
                LocalDateTime.of(2024, 5, 25, 18, 30, 0),
                ZoneId.of("Asia/Seoul")));
    }

    @Test
    @DisplayName("콘서트 회차의 예약 가능한 좌석 목록을 조회한다.")
    void getAvailableSeatsTest_seats() {
        // given
        Long concertDateId = 1L;

        // when
        when(concertRepository.findSeatsByConcertDateIdAndStatus(anyLong(), any())).thenReturn(좌석.stream().filter(v -> v.getStatus().equals(Seat.Status.AVAILABLE)).toList());
        GetSeatsResponse responses = concertService.getAvailableSeats(concertDateId);

        // then
        // 전체 5개 좌석 중 4개 좌석만 반환
        assertThat(responses.seats().size()).isEqualTo(4);
    }
}