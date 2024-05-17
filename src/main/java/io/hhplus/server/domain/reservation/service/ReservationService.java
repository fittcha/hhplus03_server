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
import io.hhplus.server.domain.concert.event.SeatStatusEvent;
import io.hhplus.server.domain.concert.service.ConcertReader;
import io.hhplus.server.domain.concert.service.ConcertService;
import io.hhplus.server.domain.outbox.entity.Outbox;
import io.hhplus.server.domain.outbox.service.OutboxService;
import io.hhplus.server.domain.payment.event.RefundPaymentEvent;
import io.hhplus.server.domain.reservation.entity.Reservation;
import io.hhplus.server.domain.reservation.event.ReservationEventPublisher;
import io.hhplus.server.domain.reservation.event.ReservationOccupiedEvent;
import io.hhplus.server.domain.reservation.repository.ReservationRepository;
import io.hhplus.server.domain.reservation.service.dto.GetReservationAndPaymentResDto;
import io.hhplus.server.domain.reservation.service.dto.SendReservationInfoDto;
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
    private final ReservationEventPublisher reservationEventPublisher;
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

        // 예약 임시 점유 event 발행
        reservationEventPublisher.reservationOccupy(new ReservationOccupiedEvent(this, reservation.getReservationId()));

        // 예약 정보를 데이터 플랫폼에 전송
        sendToDataPlatform(reservation.getReservationId(), Reservation.Status.ING);

        return ReserveResponse.from(reservation, concert, concertDate);
    }

    @Override
    @Transactional
    public void cancel(Long reservationId, CancelRequest request) {
        Reservation reservation = reservationRepository.findByIdAndUserId(reservationId, request.userId());

        // validator
        reservationValidator.isNull(reservation);

        reservation.toCancel();

        // kafka 메시지 발행 - 결제 내역 환불 처리
        Long paymentId = reservation.getPayment() == null ? null : reservation.getPayment().getPaymentId();
        String refundPaymentEventJson = JsonUtil.toJson(new RefundPaymentEvent(paymentId));
        Outbox outboxRefund = outboxService.save(Outbox.toEntity(Outbox.Type.PAYMENT_REFUND, Outbox.Status.READY, refundPaymentEventJson));
        kafkaProducer.publish(KafkaConstants.PAYMENT_REFUND_TOPIC, outboxRefund.getOutboxId(), refundPaymentEventJson);

        // kafka 메시지 발행 - 좌석 상태 변경
        SeatStatusEvent seatStatusEvent = new SeatStatusEvent(reservation.getConcertDateId(), reservation.getSeatNum(), Seat.Status.AVAILABLE);
        String seatStatusEventJson = JsonUtil.toJson(seatStatusEvent);
        Outbox outboxSeat = outboxService.save(Outbox.toEntity(Outbox.Type.SEAT_STATUS, Outbox.Status.READY, seatStatusEventJson));
        kafkaProducer.publish(KafkaConstants.SEAT_STATUS_TOPIC, outboxSeat.getOutboxId(), seatStatusEventJson);

        // 예약 정보를 데이터 플랫폼에 전송
        sendToDataPlatform(reservation.getReservationId(), Reservation.Status.CANCEL);
    }

    @Override
    public List<GetMyReservationsResponse> getMyReservations(Long userId) {
        List<GetReservationAndPaymentResDto> myReservations = reservationRepository.getMyReservations(userId);
        return myReservations.stream().map(GetMyReservationsResponse::from).toList();
    }

    private void sendToDataPlatform(Long reservationId, Reservation.Status status) {
        // Outbox 데이터 등록
        String jsonData = JsonUtil.toJson(new SendReservationInfoDto(reservationId, status));
        Outbox outbox = outboxService.save(Outbox.toEntity(Outbox.Type.SEND, Outbox.Status.READY, jsonData));

        // kafka 메시지 발행 - 예약 정보 전송
        kafkaProducer.publish(KafkaConstants.SEND_TOPIC, outbox.getOutboxId(), jsonData);
    }
}
