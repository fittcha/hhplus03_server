package io.hhplus.server.domain.unit.service;

import io.hhplus.server.base.exception.CustomException;
import io.hhplus.server.controller.reservation.dto.request.CancelRequest;
import io.hhplus.server.controller.reservation.dto.request.ReserveRequest;
import io.hhplus.server.controller.reservation.dto.response.ReserveResponse;
import io.hhplus.server.controller.user.dto.response.GetMyReservationsResponse;
import io.hhplus.server.domain.concert.entity.Concert;
import io.hhplus.server.domain.concert.entity.ConcertDate;
import io.hhplus.server.domain.concert.entity.Place;
import io.hhplus.server.domain.concert.entity.Seat;
import io.hhplus.server.domain.concert.service.ConcertReader;
import io.hhplus.server.domain.payment.entity.Payment;
import io.hhplus.server.domain.payment.service.PaymentReader;
import io.hhplus.server.domain.payment.service.PaymentService;
import io.hhplus.server.domain.payment.service.dto.CancelPaymentResultResDto;
import io.hhplus.server.domain.reservation.ReservationExceptionEnum;
import io.hhplus.server.domain.reservation.entity.Reservation;
import io.hhplus.server.domain.reservation.repository.ReservationRepository;
import io.hhplus.server.domain.reservation.service.ReservationMonitor;
import io.hhplus.server.domain.reservation.service.ReservationService;
import io.hhplus.server.domain.reservation.service.ReservationValidator;
import io.hhplus.server.domain.reservation.service.dto.GetReservationAndPaymentResDto;
import io.hhplus.server.domain.user.entity.User;
import io.hhplus.server.domain.user.service.UserReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.logging.LogLevel;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.when;
import static org.mockito.Mockito.doThrow;

class ReservationServiceTest {

    private ReservationService reservationService;
    private ReservationRepository reservationRepository;
    private ReservationValidator reservationValidator;
    private ReservationMonitor reservationMonitor;
    private ConcertReader concertReader;
    private UserReader userReader;
    private PaymentService paymentService;
    private PaymentReader paymentReader;

    private Reservation 예약건;
    private Payment 결제건;

    @BeforeEach
    void setUp() {
        // mocking
        reservationRepository = Mockito.mock(ReservationRepository.class);
        reservationValidator = Mockito.mock(ReservationValidator.class);
        reservationMonitor = Mockito.mock(ReservationMonitor.class);
        concertReader = Mockito.mock(ConcertReader.class);
        userReader = Mockito.mock(UserReader.class);
        paymentService = Mockito.mock(PaymentService.class);
        paymentReader = Mockito.mock(PaymentReader.class);

        reservationService = new ReservationService(
                reservationRepository,
                reservationValidator,
                reservationMonitor,
                concertReader,
                userReader,
                paymentService,
                paymentReader
        );

        // 예약 정보 세팅
        예약건 = Reservation.builder()
                .user(new User(1L, BigDecimal.valueOf(100000)))
                .concert(new Concert(
                        "임영웅 콘서트",
                        1L,
                        List.of(new ConcertDate(1L,
                                ZonedDateTime.of(
                                        LocalDateTime.of(2024, 5, 25, 18, 30, 0),
                                        ZoneId.of("Asia/Seoul"))))))
                .concertDate(new ConcertDate(1L,
                        ZonedDateTime.of(
                                LocalDateTime.of(2024, 5, 25, 18, 30, 0),
                                ZoneId.of("Asia/Seoul"))))
                .seat(new Seat(1L, Place.builder().build(), 1, BigDecimal.valueOf(79000)))
                .status(Reservation.Status.ING)
                .reservedAt(null)
                .build();

        // 결제 정보 세팅
        결제건 = Payment.builder()
                .reservation(예약건)
                .status(Payment.Status.READY)
                .price(BigDecimal.valueOf(79000))
                .build();
    }

    @Test
    @DisplayName("이미_선택된_좌석입니다")
    void reserveTest_이미_선택된_좌석입니다() {
        // given
        ReserveRequest request = new ReserveRequest(1L, 1L, 1L, 1L);

        // when
        when(reservationRepository.findOneByConcertDateIdAndSeatId(request.concertDateId(), request.seatId())).thenReturn(예약건);
        doThrow(new CustomException(ReservationExceptionEnum.ALREADY_RESERVED, null, LogLevel.INFO)).when(reservationValidator).checkReserved(anyLong(), anyLong());

        // then
        CustomException expected = assertThrows(CustomException.class, () ->
                reservationValidator.checkReserved(anyLong(), anyLong()));
        assertThat(expected.getMessage()).isEqualTo("이미 선택된 좌석입니다.");
    }

    @Test
    @DisplayName("좌석_예약_성공")
    void reserveTest_좌석_예약_성공() {
        // given
        ReserveRequest request = new ReserveRequest(1L, 1L, 1L, 1L);

        // when
        when(reservationRepository.findOneByConcertDateIdAndSeatId(request.concertDateId(), request.seatId())).thenReturn(null);
        when(reservationRepository.save(request.toEntity(concertReader, userReader))).thenReturn(예약건);
        when(paymentService.create(예약건.toCreatePayment())).thenReturn(결제건);
        ReserveResponse response = reservationService.reserve(request);

        // then
        assertThat(response.status()).isEqualTo(Reservation.Status.ING);
    }

    @Test
    @DisplayName("예약_취소")
    void cancelTest_예약_취소() {
        // given
        Long reservationId = 1L;
        CancelRequest request = new CancelRequest(1L);

        // when
        when(reservationRepository.findByIdAndUserId(reservationId, request.userId())).thenReturn(예약건);
        when(paymentReader.findPaymentByReservation(예약건)).thenReturn(결제건);
        when(paymentService.cancel(결제건.getPaymentId())).thenReturn(new CancelPaymentResultResDto(true, 1L, Payment.Status.CANCEL));
        reservationService.cancel(reservationId, request);
    }

    @Test
    @DisplayName("예약_내역_조회")
    void getMyReservationsTest_예약_내역_조회() {
        // given
        Long userId = 1L;

        // when
        when(reservationRepository.getMyReservations(userId)).thenReturn(List.of(new GetReservationAndPaymentResDto(예약건, 결제건)));
        List<GetMyReservationsResponse> responses = reservationService.getMyReservations(userId);

        // then
        assertThat(responses.size()).isEqualTo(1);
        assertThat(responses.get(0).concertInfo().name()).isEqualTo("임영웅 콘서트");
    }
}