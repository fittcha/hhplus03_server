package io.hhplus.server.domain.payment.repository;

import io.hhplus.server.domain.payment.entity.Payment;
import io.hhplus.server.domain.reservation.entity.Reservation;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository {

    Payment save(Payment payment);

    Payment findByReservation(Reservation reservation);

    Payment findById(Long paymentId);
}
