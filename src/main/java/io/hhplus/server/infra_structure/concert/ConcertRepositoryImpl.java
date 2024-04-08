package io.hhplus.server.infra_structure.concert;

import io.hhplus.server.domain.concert.dto.GetSeatsQueryResDto;
import io.hhplus.server.domain.concert.entity.Concert;
import io.hhplus.server.domain.concert.repository.ConcertDateJpaRepository;
import io.hhplus.server.domain.concert.repository.ConcertJpaRepository;
import io.hhplus.server.domain.concert.repository.ConcertQueryDslRepository;
import io.hhplus.server.domain.concert.repository.ConcertRepository;

import java.util.List;
import java.util.NoSuchElementException;

public class ConcertRepositoryImpl implements ConcertRepository {

    private ConcertJpaRepository concertJpaRepository;
    private ConcertDateJpaRepository concertDateJpaRepository;
    private ConcertQueryDslRepository concertQueryDslRepository;

    @Override
    public List<Concert> findAll() {
        return concertJpaRepository.findAll();
    }

    @Override
    public Concert findById(Long concertId) {
        return concertJpaRepository.findById(concertId).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public List<GetSeatsQueryResDto> getSeatsByConcertDate(Long concertId, Long concertDateId) {
        return concertQueryDslRepository.getSeatsByConcertDate(concertId, concertDateId);
    }
}
