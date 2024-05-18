package io.hhplus.server.domain.reservation.service;

import io.hhplus.server.base.kafka.KafkaConstants;
import io.hhplus.server.base.kafka.service.KafkaProducer;
import io.hhplus.server.base.redis.RedissonLock;
import io.hhplus.server.base.util.JsonUtil;
import io.hhplus.server.controller.reservation.dto.request.CancelRequest;
import io.hhplus.server.controller.reservation.dto.request.ReserveRequest;
import io.hhplus.server.controller.reservation.dto.response.ReserveResponse;
import io.hhplus.server.controller.user.dto.response.GetMyReservationsResponse;
import io.hhplus.server.domain.concert.entity.Concert;
import io.hhplus.server.domain.concert.entity.ConcertDate;
import io.hhplus.server.domain.concert.entity.Seat;
import io.hhplus.server.domain.concert.service.ConcertReader;
import io.hhplus.server.domain.concert.service.ConcertService;
import io.hhplus.server.domain.outbox.entity.Outbox;
import io.hhplus.server.domain.outbox.service.OutboxService;
import io.hhplus.server.domain.reservation.entity.Reservation;
import io.hhplus.server.domain.reservation.repository.ReservationRepository;
import io.hhplus.server.domain.reservation.service.dto.GetReservationAndPaymentResDto;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService implements ReservationInterface {

    private final ReservationRepository reservationRepository;
    private final ReservationValidator reservationValidator;
    private final ReservationMonitor reservationMonitor;
    private final ConcertReader concertReader;
    private final ConcertService concertService;
    private final OutboxService outboxService;
    private final KafkaProducer kafkaProducer;

    @PostConstruct
    public void init() {
        reservationMonitor.reservationMonitoring();
    }

    @Override
    @Transactional
    @RedissonLock(key = "'reserveLock'.concat(':').concat(#request.concertDateId()).concat('-').concat(#request.seatNum())")
    public ReserveResponse reserve(ReserveRequest request) {
        // validator
        reservationValidator.checkReserved(request.concertDateId(), request.seatNum());

        concertService.patchSeatStatus(request.concertDateId(), request.seatNum(), Seat.Status.DISABLE);
        Reservation reservation = reservationRepository.save(request.toEntity());

        Concert concert = concertReader.findConcert(reservation.getConcertId());
        ConcertDate concertDate = concertReader.findConcertDate(reservation.getConcertDateId());

        // Outbox 데이터 등록, 예약 완료 kafka 발행
        String jsonData = JsonUtil.toJson(reservation);
        Outbox outbox = outboxService.save(Outbox.toEntity(Outbox.Type.RESERVE, Outbox.Status.INIT, jsonData));
        kafkaProducer.publish(KafkaConstants.RESERVATION_TOPIC, outbox.getOutboxId(), jsonData);

        return ReserveResponse.from(reservation, concert, concertDate);
    }

    @Override
    @Transactional
    public void cancel(Long reservationId, CancelRequest request) {
        Reservation reservation = reservationRepository.findByIdAndUserId(reservationId, request.userId());

        // validator
        reservationValidator.isNull(reservation);

        reservation.toCancel();

        // Outbox 데이터 등록, 예약 취소 kafka 발행
        String jsonData = JsonUtil.toJson(reservation);
        Outbox outbox = outboxService.save(Outbox.toEntity(Outbox.Type.CANCEL, Outbox.Status.INIT, jsonData));
        kafkaProducer.publish(KafkaConstants.CANCEL_TOPIC, outbox.getOutboxId(), jsonData);
    }

    @Override
    public List<GetMyReservationsResponse> getMyReservations(Long userId) {
        List<GetReservationAndPaymentResDto> myReservations = reservationRepository.getMyReservations(userId);
        return myReservations.stream().map(GetMyReservationsResponse::from).toList();
    }
}
