package io.hhplus.server.domain.unit.service;

import io.hhplus.server.base.exception.CustomException;
import io.hhplus.server.controller.reservation.dto.request.CancelRequest;
import io.hhplus.server.controller.reservation.dto.request.ReserveRequest;
import io.hhplus.server.controller.reservation.dto.response.ReserveResponse;
import io.hhplus.server.controller.user.dto.response.GetMyReservationsResponse;
import io.hhplus.server.domain.concert.entity.Concert;
import io.hhplus.server.domain.concert.entity.ConcertDate;
import io.hhplus.server.domain.concert.entity.Seat;
import io.hhplus.server.domain.concert.service.ConcertReader;
import io.hhplus.server.domain.concert.service.ConcertService;
import io.hhplus.server.domain.outbox.entity.Outbox;
import io.hhplus.server.domain.payment.service.PaymentService;
import io.hhplus.server.domain.reservation.ReservationExceptionEnum;
import io.hhplus.server.domain.reservation.entity.Reservation;
import io.hhplus.server.domain.reservation.repository.ReservationRepository;
import io.hhplus.server.domain.reservation.service.ReservationMonitor;
import io.hhplus.server.domain.reservation.service.ReservationService;
import io.hhplus.server.domain.reservation.service.ReservationValidator;
import io.hhplus.server.domain.reservation.service.dto.GetReservationAndPaymentResDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.logging.LogLevel;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.when;
import static org.mockito.Mockito.doThrow;

class ReservationServiceTest {

    private ReservationService reservationService;
    private ReservationRepository reservationRepository;
    private ReservationValidator reservationValidator;
    private ReservationMonitor reservationMonitor;
    private ConcertReader concertReader;
    private ConcertService concertService;
    private PaymentService paymentService;
    private ApplicationEventPublisher eventPublisher;

    private Reservation 예약건;

    @BeforeEach
    void setUp() {
        // mocking
        reservationRepository = Mockito.mock(ReservationRepository.class);
        reservationValidator = Mockito.mock(ReservationValidator.class);
        reservationMonitor = Mockito.mock(ReservationMonitor.class);
        concertReader = Mockito.mock(ConcertReader.class);
        concertService = Mockito.mock(ConcertService.class);
        paymentService = Mockito.mock(PaymentService.class);
        eventPublisher = Mockito.mock(ApplicationEventPublisher.class);

        reservationService = new ReservationService(
                reservationRepository,
                reservationValidator,
                reservationMonitor,
                concertReader,
                concertService,
                paymentService,
                eventPublisher
        );

        // 예약 정보 세팅
        예약건 = Reservation.builder()
                .userId(1L)
                .concertId(1L)
                .concertDateId(1L)
                .seatNum(1)
                .status(Reservation.Status.ING)
                .reservedAt(null)
                .build();
    }

    @Test
    @DisplayName("이미 선택된 좌석입니다 예외처리")
    void reserveTest_already_reserved() {
        // given
        ReserveRequest request = new ReserveRequest(1L, 1L, 1, 1L);

        // when
        when(reservationRepository.findOneByConcertDateIdAndSeatNum(request.concertDateId(), request.seatNum())).thenReturn(예약건);
        doThrow(new CustomException(ReservationExceptionEnum.ALREADY_RESERVED, null, LogLevel.INFO)).when(reservationValidator).checkReserved(anyLong(), anyInt());

        // then
        CustomException expected = assertThrows(CustomException.class, () ->
                reservationValidator.checkReserved(anyLong(), anyInt()));
        assertThat(expected.getMessage()).isEqualTo("이미 선택된 좌석입니다.");
    }

    @Test
    @DisplayName("좌석 예약을 성공")
    void reserveTest_success() {
        // given
        ReserveRequest request = new ReserveRequest(1L, 1L, 1, 1L);
        Outbox outbox = new Outbox(1L, Outbox.Type.RESERVE, Outbox.Status.INIT, "{}");

        // when
        when(reservationRepository.findOneByConcertDateIdAndSeatNum(request.concertDateId(), request.seatNum())).thenReturn(null);
        when(reservationRepository.save(request.toEntity())).thenReturn(예약건);
        when(concertReader.findConcert(anyLong())).thenReturn(Concert.builder().build());
        when(concertReader.findConcertDate(anyLong())).thenReturn(ConcertDate.builder().build());
        ReserveResponse response = reservationService.reserve(request);

        // then
        assertThat(response.status()).isEqualTo(Reservation.Status.ING);
    }

    @Test
    @DisplayName("예약을 취소한다.")
    void cancelTest_cancel() {
        // given
        Long reservationId = 1L;
        CancelRequest request = new CancelRequest(1L);
        Outbox outbox = new Outbox(1L, Outbox.Type.CANCEL, Outbox.Status.INIT, "{}");

        // when
        when(reservationRepository.findByIdAndUserId(reservationId, request.userId())).thenReturn(예약건);
        reservationService.cancel(reservationId, request);
    }

    @Test
    @DisplayName("예약 내역 목록을 조회한다.")
    void getMyReservationsTest_my() {
        // given
        Long userId = 1L;

        // when
        when(reservationRepository.getMyReservations(userId)).thenReturn(List.of(new GetReservationAndPaymentResDto(
                예약건,
                Concert.builder().name("임영웅 콘서트").build(),
                ConcertDate.builder().build(),
                Seat.builder().build()))
        );
        List<GetMyReservationsResponse> responses = reservationService.getMyReservations(userId);

        // then
        assertThat(responses.size()).isEqualTo(1);
        assertThat(responses.get(0).concertInfo().name()).isEqualTo("임영웅 콘서트");
    }
}