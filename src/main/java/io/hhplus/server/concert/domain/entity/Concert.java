package io.hhplus.server.concert.domain.entity;

import io.hhplus.server.BaseDateTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
@Table(name = "concert")
public class Concert extends BaseDateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long concertId;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private int ticketPrice = 0;

    @Column(nullable = false)
    private String place;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "concert_id")
    private List<ConcertDate> concertDates = new ArrayList();

    public Concert(String name, int ticketPrice, String place, String lineUp) {
        this.name = name;
        this.ticketPrice = ticketPrice;
        this.place = place;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Concert concert = (Concert) o;
        return Objects.equals(concertId, concert.concertId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(concertId);
    }
}
