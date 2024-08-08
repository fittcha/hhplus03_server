package io.hhplus.concert.domain.reservation.repository;

import io.hhplus.concert.domain.reservation.entity.Reservation;
import io.hhplus.concert.domain.reservation.service.dto.GetReservationAndPaymentResDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReservationJpaRepository extends JpaRepository<Reservation, Long> {

    Reservation findByReservationIdAndUserId(Long reservationId, Long userId);

    @Query("SELECT new io.hhplus.concert.domain.reservation.service.dto.GetReservationAndPaymentResDto(r, c, cd, s)" +
            "FROM Reservation r " +
            "JOIN Concert c on c.concertId = r.concertId " +
            "JOIN ConcertDate cd on cd.concertDateId = r.concertDateId " +
            "JOIN Seat s on s.concertDate.concertDateId = cd.concertDateId and s.seatNum = r.seatNum " +
            "WHERE r.userId = :userId")
    List<GetReservationAndPaymentResDto> getMyReservations(Long userId);

    Reservation findOneByConcertDateIdAndSeatNum(Long concertDateId, int seatNum);
}
