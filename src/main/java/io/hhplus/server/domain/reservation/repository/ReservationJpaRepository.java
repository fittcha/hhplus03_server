package io.hhplus.server.domain.reservation.repository;

import io.hhplus.server.domain.reservation.entity.Reservation;
import io.hhplus.server.domain.reservation.service.dto.GetReservationAndPaymentResDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReservationJpaRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findAllByConcertDateId(Long concertDateId);

    Reservation findOneByConcertDateIdAndSeatId(Long concertDateId, Long seatId);

    Reservation findByReservationIdAndUserId(Long reservationId, Long userId);

    @Query("SELECT new io.hhplus.server.domain.reservation.service.dto.GetReservationAndPaymentResDto(r, c, cd, s)" +
            "FROM Reservation r " +
            "JOIN Concert c on c.concertId = r.concertId " +
            "JOIN ConcertDate cd on cd.concertDateId = r.concertDateId " +
            "JOIN Seat s on s.seatId = r.seatId " +
            "WHERE r.userId = :userId")
    List<GetReservationAndPaymentResDto> getMyReservations(Long userId);
}
