package io.hhplus.server.domain.payment.entity;

import io.hhplus.server.base.entity.BaseDateTimeEntity;
import io.hhplus.server.domain.reservation.entity.Reservation;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
@Table(name = "payment")
public class Payment extends BaseDateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @OneToOne()
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @Column(nullable = false)
    private Payment.Status status;

    @Column(nullable = false)
    private BigDecimal price;

    private ZonedDateTime paidAt;

    public enum Status {
        READY,
        COMPLETE,
        CANCEL,
        REFUND
    }

    @Builder
    public Payment(Reservation reservation, Payment.Status status, BigDecimal price) {
        this.reservation = reservation;
        this.status = status;
        this.price = price;
    }

    public Payment updateStatus(Payment.Status status) {
        if (status == null) {
            return null;
        }

        this.status = status;
        return this;
    }

    public Payment toPaid() {
        this.status = Payment.Status.COMPLETE;
        this.paidAt = ZonedDateTime.now();
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payment that = (Payment) o;
        return Objects.equals(paymentId, that.paymentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paymentId);
    }
}
