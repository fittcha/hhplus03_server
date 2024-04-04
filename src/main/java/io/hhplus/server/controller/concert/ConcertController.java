package io.hhplus.server.controller.concert;

import io.hhplus.server.controller.concert.dto.response.GetConcertResponse;
import io.hhplus.server.controller.concert.dto.response.GetConcertsResponse;
import io.hhplus.server.controller.concert.dto.response.GetDatesResponse;
import io.hhplus.server.controller.concert.dto.response.GetSeatsResponse;
import io.hhplus.server.domain.concert.service.ConcertService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/concerts")
@RestController
@RequiredArgsConstructor
public class ConcertController {

    private final ConcertService service;

    @GetMapping("/")
    public List<GetConcertsResponse> getConcerts() {
        return service.getConcerts();
    }

    @GetMapping("/{concertId}")
    public GetConcertResponse getConcert(@PathVariable(value = "concertId") @NotNull Long concertId) {
        return service.getConcert(concertId);
    }

    @GetMapping("/{concertId}/dates")
    public List<GetDatesResponse> getDates(@PathVariable(value = "concertId") @NotNull Long concertId) {
        return service.getDates(concertId);
    }

    @GetMapping("/{concertId}/dates/{concertDateId}/seats")
    public List<GetSeatsResponse> getSeats(@PathVariable(value = "concertId") @NotNull Long concertId,
                                     @PathVariable(value = "concertDateId") @NotNull Long concertDateId) {
        return service.getSeats(concertId, concertDateId);
    }
}
