package io.hhplus.concert.domain.payment.repository;

import io.hhplus.concert.domain.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentJpaRepository extends JpaRepository<Payment, Long> {

    Payment findByReservation_ReservationId(Long reservationId);
}
