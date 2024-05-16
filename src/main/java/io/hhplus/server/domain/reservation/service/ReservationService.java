package io.hhplus.server.domain.reservation.service;

import com.google.gson.Gson;
import io.hhplus.server.base.redis.RedissonLock;
import io.hhplus.server.controller.reservation.dto.request.CancelRequest;
import io.hhplus.server.controller.reservation.dto.request.ReserveRequest;
import io.hhplus.server.controller.reservation.dto.response.ReserveResponse;
import io.hhplus.server.controller.user.dto.response.GetMyReservationsResponse;
import io.hhplus.server.domain.concert.entity.Concert;
import io.hhplus.server.domain.concert.entity.ConcertDate;
import io.hhplus.server.domain.concert.entity.Seat;
import io.hhplus.server.domain.concert.service.ConcertReader;
import io.hhplus.server.domain.concert.service.ConcertService;
import io.hhplus.server.domain.reservation.entity.Reservation;
import io.hhplus.server.domain.reservation.event.ReservationCancelledEvent;
import io.hhplus.server.domain.reservation.event.ReservationOccupiedEvent;
import io.hhplus.server.domain.reservation.repository.ReservationRepository;
import io.hhplus.server.domain.reservation.service.dto.GetReservationAndPaymentResDto;
import io.hhplus.server.domain.reservation.service.dto.SendReservationInfoDto;
import io.hhplus.server.domain.send.dto.SendCommReqDto;
import io.hhplus.server.domain.send.entity.Send;
import io.hhplus.server.domain.send.event.SendEvent;
import io.hhplus.server.domain.send.service.SendService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
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
    private final SendService sendService;
    private final ApplicationEventPublisher eventPublisher;

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
        eventPublisher.publishEvent(new ReservationOccupiedEvent(this, reservation.getReservationId()));

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

        // 결제 내역 환불 처리 event
        eventPublisher.publishEvent(new ReservationCancelledEvent(this, reservationId));

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
        Gson gson = new Gson();
        String jsonData = gson.toJson(new SendReservationInfoDto(reservationId, status));
        Send send = sendService.save(Send.toEntity(Send.Type.RESERVATION, Send.Status.READY, jsonData));

        // 예약 정보 전송 event 발행
        eventPublisher.publishEvent(new SendEvent(this, new SendCommReqDto(send.getSendId(), jsonData)));
    }
}
