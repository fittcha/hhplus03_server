package io.hhplus.server.infra_structure;

import io.hhplus.server.domain.payment.repository.PaymentJpaRepository;
import io.hhplus.server.domain.payment.repository.PaymentRepository;

public class PaymentRepositoryImpl implements PaymentRepository {

    private PaymentJpaRepository paymentJpaRepository;
}
