package io.hhplus.server.domain.reservation.service;

import io.hhplus.server.controller.reservation.dto.request.CancelRequest;
import io.hhplus.server.controller.reservation.dto.request.ReserveRequest;
import io.hhplus.server.controller.reservation.dto.response.ReserveResponse;
import io.hhplus.server.domain.reservation.entity.Reservation;
import io.hhplus.server.domain.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService implements ReservationInterface {

    private final ReservationRepository reservationRepository;

    @Override
    public ReserveResponse reserve(ReserveRequest request) {
        return null;
    }

    @Override
    public void cancel(Long reservationId, CancelRequest request) {

    }

    @Override
    public List<Reservation> getReservationsByConcertDate(Long concertDateId) {
        return reservationRepository.findAllByConcertDateId(concertDateId);
    }
}
