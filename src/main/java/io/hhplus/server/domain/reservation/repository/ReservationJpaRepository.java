package io.hhplus.server.domain.reservation.repository;

import io.hhplus.server.domain.reservation.entity.Reservation;
import io.hhplus.server.domain.reservation.service.dto.GetReservationAndPaymentResDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReservationJpaRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findAllByConcertDate_ConcertDateId(Long concertDateId);

    Reservation findOneByConcertDate_ConcertDateIdAndSeat_SeatId(Long concertDateId, Long seatId);

    Reservation findByReservationIdAndUser_UserId(Long reservationId, Long userId);

    @Query("SELECT new io.hhplus.server.domain.reservation.service.dto.GetReservationAndPaymentResDto(r, p)" +
            "FROM Reservation r " +
            "JOIN Payment p on p.reservation.reservationId = r.reservationId " +
            "WHERE r.user.userId = :userId")
    List<GetReservationAndPaymentResDto> getMyReservations(Long userId);
}
