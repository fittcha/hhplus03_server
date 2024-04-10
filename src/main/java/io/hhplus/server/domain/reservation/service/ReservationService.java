package io.hhplus.server.domain.reservation.service;

import io.hhplus.server.controller.reservation.dto.request.CancelRequest;
import io.hhplus.server.controller.reservation.dto.request.ReserveRequest;
import io.hhplus.server.controller.reservation.dto.response.ReserveResponse;
import io.hhplus.server.domain.concert.service.ConcertReader;
import io.hhplus.server.domain.payment.entity.Payment;
import io.hhplus.server.domain.payment.service.PaymentManager;
import io.hhplus.server.domain.payment.service.dto.CancelPaymentResultResDto;
import io.hhplus.server.domain.reservation.entity.Reservation;
import io.hhplus.server.domain.reservation.repository.ReservationRepository;
import io.hhplus.server.domain.user.service.UserReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationService implements ReservationInterface {

    private final ReservationRepository reservationRepository;
    private final ReservationValidator reservationValidator;
    private final ConcertReader concertReader;
    private final UserReader userReader;
    private final PaymentManager paymentManager;

    @Override
    public ReserveResponse reserve(ReserveRequest request) {
        // validator
        reservationValidator.checkReserved(request.concertDateId(), request.seatId());

        // 좌석 예약
        Reservation reservation = reservationRepository.save(request.toEntity(concertReader, userReader));
        // 결제 정보 생성
        Payment payment = paymentManager.create(reservation.toCreatePayment());
        // TODO - 5분 선점

        return ReserveResponse.from(reservation, payment);
    }

    @Override
    public void cancel(Long reservationId, CancelRequest request) {
        Reservation reservation = reservationRepository.findByIdAndUserId(reservationId, request.userId());

        // validator
        reservationValidator.isNull(reservation);

        // 취소
        // 1. 결제 정보 처리
        Payment payment = paymentManager.getPaymentByReservation(reservation);
        CancelPaymentResultResDto cancelPaymentInfo = paymentManager.cancel(payment);
        if (cancelPaymentInfo.isSuccess()) { // 결제 정보 처리 성공 시
            // 2. 예약 내역 삭제
            reservationRepository.delete(reservation);
        }
    }
}
