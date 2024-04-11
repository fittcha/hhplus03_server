package io.hhplus.server.domain.payment.service;

import io.hhplus.server.domain.payment.entity.Payment;
import io.hhplus.server.domain.payment.repository.PaymentRepository;
import io.hhplus.server.domain.reservation.entity.Reservation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentReader {
    /* Payment 관련 정보 단순 조회용 */

    private final PaymentRepository paymentRepository;

    /* 예약 건으로 결제 정보 조회 */
    public Payment findPaymentByReservation(Reservation reservation) {
        return paymentRepository.findByReservation(reservation);
    }
}
