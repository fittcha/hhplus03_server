package io.hhplus.server.infra_structure.payment;

import io.hhplus.server.domain.payment.entity.Payment;
import io.hhplus.server.domain.payment.repository.PaymentJpaRepository;
import io.hhplus.server.domain.payment.repository.PaymentRepository;
import io.hhplus.server.domain.reservation.entity.Reservation;
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
    public Payment findByReservation(Reservation reservation) {
        return paymentJpaRepository.findByReservation(reservation);
    }

    @Override
    public Payment findById(Long paymentId) {
        return paymentJpaRepository.findById(paymentId).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public void delete(Payment payment) {
        paymentJpaRepository.delete(payment);
    }
}
