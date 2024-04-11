package io.hhplus.server.domain.reservation.entity;

import io.hhplus.server.base.entity.BaseDateTimeEntity;
import io.hhplus.server.domain.concert.entity.Concert;
import io.hhplus.server.domain.concert.entity.ConcertDate;
import io.hhplus.server.domain.concert.entity.Seat;
import io.hhplus.server.domain.payment.entity.Payment;
import io.hhplus.server.domain.payment.service.dto.CreatePaymentReqDto;
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

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "concert_id")
    private Concert concert;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "concert_date_id")
    private ConcertDate concertDate;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "seat_id")
    private Seat seat;

    @Column(nullable = false)
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

    public CreatePaymentReqDto toCreatePayment() {
        return new CreatePaymentReqDto(this, Payment.Status.READY, this.seat.getPrice());
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
