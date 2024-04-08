package io.hhplus.server.domain.concert.repository;

import io.hhplus.server.domain.concert.dto.GetSeatsQueryResDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConcertQueryDslRepository {

    List<GetSeatsQueryResDto> getSeatsByConcertDate(Long concertId, Long concertDateId);
}
