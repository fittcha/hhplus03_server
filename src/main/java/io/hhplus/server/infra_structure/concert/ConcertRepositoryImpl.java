package io.hhplus.server.infra_structure.concert;

import io.hhplus.server.domain.concert.entity.Concert;
import io.hhplus.server.domain.concert.repository.ConcertDateJpaRepository;
import io.hhplus.server.domain.concert.repository.ConcertJpaRepository;
import io.hhplus.server.domain.concert.repository.ConcertRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.NoSuchElementException;

@Repository
public class ConcertRepositoryImpl implements ConcertRepository {

    private final ConcertJpaRepository concertJpaRepository;

    public ConcertRepositoryImpl(ConcertJpaRepository concertJpaRepository, ConcertDateJpaRepository concertDateJpaRepository) {
        this.concertJpaRepository = concertJpaRepository;
    }

    @Override
    public List<Concert> findAll() {
        return concertJpaRepository.findAll();
    }

    @Override
    public Concert findById(Long concertId) {
        return concertJpaRepository.findById(concertId).orElseThrow(NoSuchElementException::new);
    }
}
