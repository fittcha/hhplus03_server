package io.hhplus.server.domain.payment.repository;

import io.hhplus.server.domain.payment.entity.Payment;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository {

    Payment save(Payment payment);

    Payment findByReservationId(Long reservationId);

    Payment findById(Long paymentId);
}
