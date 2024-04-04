package io.hhplus.server.infra_structure;

import io.hhplus.server.domain.reservation.repository.ReservationJpaRepository;
import io.hhplus.server.domain.reservation.repository.ReservationRepository;

public class ReservationRepositoryImpl implements ReservationRepository {

    private ReservationJpaRepository reservationJpaRepository;
}
