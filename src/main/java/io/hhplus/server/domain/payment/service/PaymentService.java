package io.hhplus.server.domain.payment.service;

import io.hhplus.server.controller.payment.dto.request.PayRequest;
import io.hhplus.server.controller.payment.dto.response.PayResponse;
import io.hhplus.server.domain.payment.entity.Payment;
import io.hhplus.server.domain.payment.repository.PaymentRepository;
import io.hhplus.server.domain.payment.service.dto.CancelPaymentResultResDto;
import io.hhplus.server.domain.payment.service.dto.CreatePaymentReqDto;
import io.hhplus.server.domain.user.entity.User;
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

    @Override
    @Transactional
    public PayResponse pay(Long paymentId, PayRequest request) {
        // validator - 결제 상태 검증
        Payment payment = paymentRepository.findById(paymentId);
        paymentValidator.checkPayStatus(payment.getStatus());

        // validator - 사용자 잔액 검증
        User user = userReader.findUser(request.userId());
        paymentValidator.checkBalance(payment.getPrice(), user.getBalance());

        // 결제
        // 1. 결제
        boolean isSuccess = false;
        Payment paymentResult = payment.toPaid();   // 결제 완료 처리
        paymentResult.getReservation().toComplete();    // 예약 완료 처리
        BigDecimal usedBalance = user.getBalance();
        if (paymentResult.getStatus().equals(Payment.Status.COMPLETE)) {
            // 2. 사용자 잔액 차감
            usedBalance = user.useBalance(payment.getPrice());
            isSuccess = true;
        }

        return PayResponse.from(isSuccess, paymentResult, usedBalance);
    }

    @Override
    public Payment create(CreatePaymentReqDto reqDto) {
        return paymentRepository.save(reqDto.toEntity());
    }

    @Override
    @Transactional
    public CancelPaymentResultResDto cancel(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId);

        // validator
        paymentValidator.checkCancelStatus(payment.getStatus());

        // 취소
        Payment updatedPayment = cancelPayment(payment);

        // 성공 / 실패 응답 반환
        boolean isSuccess = updatedPayment != null;
        if (isSuccess) {
            return new CancelPaymentResultResDto(true, updatedPayment.getPaymentId(), updatedPayment.getStatus());
        } else {
            return new CancelPaymentResultResDto(false, payment.getPaymentId(), payment.getStatus());
        }
    }

    private Payment cancelPayment(Payment payment) {
        Payment updatedPayment = payment;
        User user = payment.getReservation().getUser();

        if (Payment.Status.READY.equals(payment.getStatus())) {
            // 결제 대기 상태일 경우 - 즉시 취소
            updatedPayment = payment.updateStatus(Payment.Status.CANCEL);
        } else if (Payment.Status.COMPLETE.equals(payment.getStatus())) {
            // 결제 완료 상태일 경우 - 환불
            updatedPayment = payment.updateStatus(Payment.Status.REFUND);
            // 잔액 환불
            user.refundBalance(payment.getPrice());
        }

        return updatedPayment;
    }
}
