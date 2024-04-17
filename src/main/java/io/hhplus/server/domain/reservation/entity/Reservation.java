package io.hhplus.server.domain.reservation.entity;

import io.hhplus.server.base.entity.BaseDateTimeEntity;
import io.hhplus.server.domain.concert.entity.Concert;
import io.hhplus.server.domain.concert.entity.ConcertDate;
import io.hhplus.server.domain.concert.entity.Seat;
import io.hhplus.server.domain.user.entity.User;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_id")
    private Concert concert;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_date_id")
    private ConcertDate concertDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id")
    private Seat seat;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Reservation.Status status;

    private ZonedDateTime reservedAt;

    public void toComplete() {
        this.status = Status.RESERVED;
    }

    public enum Status {
        ING,
        RESERVED,
        CANCEL
    }
    
    @Builder
    public Reservation(User user, Concert concert, ConcertDate concertDate, Seat seat, Reservation.Status status, ZonedDateTime reservedAt) {
        this.user = user;
        this.concert = concert;
        this.concertDate = concertDate;
        this.seat = seat;
        this.status = status;
        this.reservedAt = reservedAt;
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
