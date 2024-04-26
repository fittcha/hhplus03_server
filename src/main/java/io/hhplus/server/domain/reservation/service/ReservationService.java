package io.hhplus.server.domain.reservation.service;

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
import io.hhplus.server.domain.payment.entity.Payment;
import io.hhplus.server.domain.payment.service.PaymentReader;
import io.hhplus.server.domain.payment.service.PaymentService;
import io.hhplus.server.domain.reservation.ReservationExceptionEnum;
import io.hhplus.server.domain.reservation.entity.Reservation;
import io.hhplus.server.domain.reservation.event.ReservationOccupiedEvent;
import io.hhplus.server.domain.reservation.repository.ReservationRepository;
import io.hhplus.server.domain.reservation.service.dto.GetReservationAndPaymentResDto;
import io.hhplus.server.domain.user.service.UserReader;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.logging.LogLevel;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService implements ReservationInterface {

    private final ReservationRepository reservationRepository;
    private final ReservationValidator reservationValidator;
    private final ReservationMonitor reservationMonitor;
    private final ConcertReader concertReader;
    private final ConcertService concertService;
    private final UserReader userReader;
    private final PaymentService paymentService;
    private final PaymentReader paymentReader;
    private final ApplicationEventPublisher eventPublisher;

    @PostConstruct
    public void init() {
        reservationMonitor.reservationMonitoring();
    }

    @Override
    @Transactional
    public ReserveResponse reserve(ReserveRequest request) {
        try {
            // validator
            reservationValidator.checkReserved(request.concertDateId(), request.seatNum());

            Reservation reservation = reservationRepository.save(request.toEntity(concertReader, userReader));
            concertService.patchSeatStatus(reservation.getConcertDateId(), reservation.getSeatNum(), Seat.Status.DISABLE);

            Concert concert = concertReader.findConcert(reservation.getConcertId());
            ConcertDate concertDate = concertReader.findConcertDate(reservation.getConcertDateId());
            Seat seat = concertReader.findSeat(reservation.getConcertDateId(), reservation.getSeatNum());

            // 예약 임시 점유 event 발행
            eventPublisher.publishEvent(new ReservationOccupiedEvent(this, reservation.getReservationId()));

            return ReserveResponse.from(reservation, concert, concertDate, seat);

        } catch (DataIntegrityViolationException e) {
            // 유니크 제약 조건(concertDateId, seatId) 위반 시
            throw new CustomException(ReservationExceptionEnum.ALREADY_RESERVED, null, LogLevel.INFO);
        }
    }

    @Override
    @Transactional
    public void cancel(Long reservationId, CancelRequest request) {
        Reservation reservation = reservationRepository.findByIdAndUserId(reservationId, request.userId());

        // validator
        reservationValidator.isNull(reservation);

        Payment payment = paymentReader.findPaymentByReservation(reservation);
        if (payment != null) {
            // 결제 내역 존재하면 환불 처리
            paymentService.cancel(payment.getPaymentId());
        }
        reservationRepository.delete(reservation);
    }

    @Override
    public List<GetMyReservationsResponse> getMyReservations(Long userId) {
        List<GetReservationAndPaymentResDto> myReservations = reservationRepository.getMyReservations(userId);
        return myReservations.stream().map(GetMyReservationsResponse::from).toList();
    }
}
