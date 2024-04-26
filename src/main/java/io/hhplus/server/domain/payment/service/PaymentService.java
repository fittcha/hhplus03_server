package io.hhplus.server.domain.payment.service;

import io.hhplus.server.controller.payment.dto.request.CreateRequest;
import io.hhplus.server.controller.payment.dto.request.PayRequest;
import io.hhplus.server.controller.payment.dto.response.CreateResponse;
import io.hhplus.server.controller.payment.dto.response.PayResponse;
import io.hhplus.server.domain.payment.entity.Payment;
import io.hhplus.server.domain.payment.repository.PaymentRepository;
import io.hhplus.server.domain.payment.service.dto.CancelPaymentResultResDto;
import io.hhplus.server.domain.reservation.entity.Reservation;
import io.hhplus.server.domain.reservation.service.ReservationReader;
import io.hhplus.server.domain.user.entity.Users;
import io.hhplus.server.domain.user.service.UserReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PaymentService implements PaymentInterface {

    private final PaymentRepository paymentRepository;
    private final PaymentValidator paymentValidator;
    private final UserReader userReader;
    private final ReservationReader reservationReader;

    @Override
    @Transactional
    public PayResponse pay(Long paymentId, PayRequest request) {
        // validator - 결제 상태 검증
        Payment payment = paymentRepository.findById(paymentId);
        paymentValidator.checkPayStatus(payment.getStatus());

        // validator - 사용자 잔액 검증
        Users users = userReader.findUser(request.userId());
        paymentValidator.checkBalance(payment.getPrice(), users.getBalance());

        boolean isSuccess = false;
        // 1. 사용자 잔액 차감
        BigDecimal previousBalance = users.getBalance();
        BigDecimal usedBalance = users.useBalance(payment.getPrice());
        if (usedBalance.equals(previousBalance.subtract(payment.getPrice()))) {
            // 2-1. 결제 완료 처리
            payment = payment.toPaid();
            payment.getReservation().toComplete();
            isSuccess = true;
        } else {
            // 2-2. 결제 실패 : 잔액 원복
            usedBalance = users.getBalance();
        }

        return PayResponse.from(isSuccess, payment, usedBalance);
    }

    @Override
    @Transactional
    public CreateResponse create(CreateRequest request) {
        Reservation reservation = reservationReader.findReservation(request.reservationId());
        Payment payment = paymentRepository.save(request.toEntity(reservation));
        if (payment == null) {
            return new CreateResponse(null);
        }
        return new CreateResponse(payment.getPaymentId());
    }

    @Override
    @Transactional
    public CancelPaymentResultResDto cancel(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId);

        // validator
        paymentValidator.checkCancelStatus(payment.getStatus());

        Payment updatedPayment = cancelPayment(payment);
        boolean isSuccess = updatedPayment != null;
        if (isSuccess) {
            return new CancelPaymentResultResDto(true, updatedPayment.getPaymentId(), updatedPayment.getStatus());
        } else {
            return new CancelPaymentResultResDto(false, payment.getPaymentId(), payment.getStatus());
        }
    }

    private Payment cancelPayment(Payment payment) {
        Payment updatedPayment = payment;
        Long userId = payment.getReservation().getUserId();
        Users users = userReader.findUser(userId);

        if (Payment.Status.READY.equals(payment.getStatus())) {
            // 결제 대기 상태 - 즉시 취소
            updatedPayment = payment.updateStatus(Payment.Status.CANCEL);
        } else if (Payment.Status.COMPLETE.equals(payment.getStatus())) {
            // 결제 완료 상태 - 환불
            updatedPayment = payment.updateStatus(Payment.Status.REFUND);
            users.refundBalance(payment.getPrice());
        }

        return updatedPayment;
    }
}
