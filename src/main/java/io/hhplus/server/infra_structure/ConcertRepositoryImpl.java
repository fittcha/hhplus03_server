package io.hhplus.server.infra_structure;

import io.hhplus.server.domain.concert.repository.ConcertDateJpaRepository;
import io.hhplus.server.domain.concert.repository.ConcertJpaRepository;
import io.hhplus.server.domain.concert.repository.ConcertRepository;
import io.hhplus.server.domain.concert.repository.SeatJpaRepository;

public class ConcertRepositoryImpl implements ConcertRepository {

    private ConcertJpaRepository concertJpaRepository;
    private ConcertDateJpaRepository concertDateJpaRepository;
    private SeatJpaRepository seatJpaRepository;
}
