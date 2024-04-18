package io.hhplus.server.domain.reservation.entity;

import io.hhplus.server.base.entity.BaseDateTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
@Table(name = "reservation")
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
    private Long seatId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Reservation.Status status;

    private ZonedDateTime reservedAt;

    @Builder
    public Reservation(Long userId, Long concertId, Long concertDateId, Long seatId, Status status, ZonedDateTime reservedAt) {
        this.userId = userId;
        this.concertId = concertId;
        this.concertDateId = concertDateId;
        this.seatId = seatId;
        this.status = status;
        this.reservedAt = reservedAt;
    }

    public void toComplete() {
        this.status = Status.RESERVED;
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
