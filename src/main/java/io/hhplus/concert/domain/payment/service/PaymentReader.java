package io.hhplus.concert.domain.payment.service;

import io.hhplus.concert.domain.payment.entity.Payment;
import io.hhplus.concert.domain.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentReader {
    /* Payment 관련 정보 단순 조회용 */

    private final PaymentRepository paymentRepository;

    /* 예약 건으로 결제 정보 조회 */
    public Payment findPaymentByReservationId(Long reservationId) {
        return paymentRepository.findByReservationId(reservationId);
    }
}
