package io.hhplus.concert.domain.reservation.entity;

import io.hhplus.concert.base.entity.BaseDateTimeEntity;
import io.hhplus.concert.domain.payment.entity.Payment;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
public class Reservation extends BaseDateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long concertId;

    @Column(nullable = false)
    private Long concertDateId;

    @Column(nullable = false)
    private int seatNum;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Reservation.Status status;

    private ZonedDateTime reservedAt;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.REMOVE)
    private List<Payment> paymentList = new ArrayList<>();

    @Builder
    public Reservation(Long userId, Long concertId, Long concertDateId, int seatNum, Status status, ZonedDateTime reservedAt) {
        this.userId = userId;
        this.concertId = concertId;
        this.concertDateId = concertDateId;
        this.seatNum = seatNum;
        this.status = status;
        this.reservedAt = reservedAt;
    }

    public void toComplete() {
        this.status = Status.RESERVED;
    }

    public void toCancel() {
        this.status = Status.CANCEL;
    }

    public enum Status {
        ING,
        RESERVED,
        CANCEL
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return Objects.equals(reservationId, that.reservationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reservationId);
    }
}
