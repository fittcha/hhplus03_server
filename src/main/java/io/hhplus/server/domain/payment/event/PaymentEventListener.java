package io.hhplus.server.domain.payment.event;

import io.hhplus.server.domain.payment.service.PaymentService;
import io.hhplus.server.domain.reservation.event.ReservationCancelledEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class PaymentEventListener {

    private final PaymentService paymentService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onReservationCancelledEvent(ReservationCancelledEvent event) {
        paymentService.refundReservationCancelled(event.getReservationId());
    }
}

