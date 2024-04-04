package io.hhplus.server.controller.reservation;

import io.hhplus.server.controller.reservation.dto.request.CancelRequest;
import io.hhplus.server.controller.reservation.dto.request.ReserveRequest;
import io.hhplus.server.controller.reservation.dto.response.ReserveResponse;
import io.hhplus.server.domain.reservation.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/reservations")
@RestController
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService service;

    @PostMapping("/")
    public ReserveResponse reserve(@RequestBody @Valid ReserveRequest request) {
        return service.reserve(request);
    }

    @DeleteMapping("/{reservationId}")
    public void cancel(@PathVariable(value = "reservationId") Long reservationId,
                       @RequestBody @Valid CancelRequest request) {
        service.cancel(reservationId, request);
    }
}
