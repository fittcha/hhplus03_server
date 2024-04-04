package io.hhplus.server.domain.concert.entity;

import io.hhplus.server.BaseDateTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "concert_date")
public class ConcertDate extends BaseDateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long concertDateId;

    @Column(nullable = false)
    private ZonedDateTime concertDate;

    public ConcertDate(ZonedDateTime concertDate, int seats) {
        this.concertDate = concertDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConcertDate that = (ConcertDate) o;
        return Objects.equals(concertDateId, that.concertDateId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(concertDateId);
    }
}
