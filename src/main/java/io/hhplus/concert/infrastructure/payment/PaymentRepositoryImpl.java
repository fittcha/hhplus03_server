package io.hhplus.concert.infrastructure.payment;

import io.hhplus.concert.domain.payment.entity.Payment;
import io.hhplus.concert.domain.payment.repository.PaymentJpaRepository;
import io.hhplus.concert.domain.payment.repository.PaymentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Repository;

@Repository
public class PaymentRepositoryImpl implements PaymentRepository {

    private final PaymentJpaRepository paymentJpaRepository;

    public PaymentRepositoryImpl(PaymentJpaRepository paymentJpaRepository) {
        this.paymentJpaRepository = paymentJpaRepository;
    }

    @Override
    public Payment save(Payment payment) {
        return paymentJpaRepository.save(payment);
    }

    @Override
    public Payment findByReservationId(Long reservationId) {
        return paymentJpaRepository.findByReservation_ReservationId(reservationId);
    }

    @Override
    public Payment findById(Long paymentId) {
        return paymentJpaRepository.findById(paymentId).orElseThrow(EntityNotFoundException::new);
    }
}
