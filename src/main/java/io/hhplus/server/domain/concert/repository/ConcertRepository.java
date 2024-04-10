package io.hhplus.server.domain.concert.repository;

import io.hhplus.server.domain.concert.entity.Concert;
import io.hhplus.server.domain.concert.entity.ConcertDate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConcertRepository {

    List<Concert> findAll();

    Concert findById(Long concertId);

    ConcertDate findConcertDateById(Long concertDateId);
}
