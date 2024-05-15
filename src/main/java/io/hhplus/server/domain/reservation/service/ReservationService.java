package io.hhplus.server.domain.reservation.service;

import io.hhplus.server.base.redis.RedissonLock;
import io.hhplus.server.controller.reservation.dto.request.CancelRequest;
import io.hhplus.server.controller.reservation.dto.request.ReserveRequest;
import io.hhplus.server.controller.reservation.dto.response.ReserveResponse;
import io.hhplus.server.controller.user.dto.response.GetMyReservationsResponse;
import io.hhplus.server.domain.concert.entity.Concert;
import io.hhplus.server.domain.concert.entity.ConcertDate;
import io.hhplus.server.domain.concert.entity.Seat;
import io.hhplus.server.domain.concert.service.ConcertReader;
import io.hhplus.server.domain.concert.service.ConcertService;
import io.hhplus.server.domain.payment.service.PaymentReader;
import io.hhplus.server.domain.payment.service.PaymentService;
import io.hhplus.server.domain.reservation.entity.Reservation;
import io.hhplus.server.domain.reservation.event.ReservationCancelledEvent;
import io.hhplus.server.domain.reservation.event.ReservationOccupiedEvent;
import io.hhplus.server.domain.reservation.repository.ReservationRepository;
import io.hhplus.server.domain.reservation.service.dto.GetReservationAndPaymentResDto;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
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
    private final PaymentService paymentService;
    private final PaymentReader paymentReader;
    private final ApplicationEventPublisher eventPublisher;

    @PostConstruct
    public void init() {
        reservationMonitor.reservationMonitoring();
    }

    @Override
    @Transactional
    @RedissonLock(key = "'reserveLock'.concat(':').concat(#request.concertDateId()).concat('-').concat(#request.seatNum())")
    public ReserveResponse reserve(ReserveRequest request) {
        // validator
        reservationValidator.checkReserved(request.concertDateId(), request.seatNum());

        concertService.patchSeatStatus(request.concertDateId(), request.seatNum(), Seat.Status.DISABLE);
        Reservation reservation = reservationRepository.save(request.toEntity());

        Concert concert = concertReader.findConcert(reservation.getConcertId());
        ConcertDate concertDate = concertReader.findConcertDate(reservation.getConcertDateId());

        // 예약 임시 점유 event 발행
        eventPublisher.publishEvent(new ReservationOccupiedEvent(this, reservation.getReservationId()));

        return ReserveResponse.from(reservation, concert, concertDate);
    }

    @Override
    @Transactional
    public void cancel(Long reservationId, CancelRequest request) {
        Reservation reservation = reservationRepository.findByIdAndUserId(reservationId, request.userId());

        // validator
        reservationValidator.isNull(reservation);

        reservation.toCancel();

        // 결제 내역 환불 처리 event
        eventPublisher.publishEvent(new ReservationCancelledEvent(this, reservationId));
    }

    @Override
    public List<GetMyReservationsResponse> getMyReservations(Long userId) {
        List<GetReservationAndPaymentResDto> myReservations = reservationRepository.getMyReservations(userId);
        return myReservations.stream().map(GetMyReservationsResponse::from).toList();
    }
}
