package io.hhplus.server.domain.payment.repository;

import io.hhplus.server.domain.payment.entity.Payment;
import io.hhplus.server.domain.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentJpaRepository extends JpaRepository<Payment, Long> {

    Payment findByReservation(Reservation reservation);
}
