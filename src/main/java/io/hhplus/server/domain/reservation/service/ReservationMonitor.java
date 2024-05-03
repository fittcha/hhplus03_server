package io.hhplus.server.domain.reservation.service;

import io.hhplus.server.domain.concert.entity.Seat;
import io.hhplus.server.domain.concert.service.ConcertService;
import io.hhplus.server.domain.payment.entity.Payment;
import io.hhplus.server.domain.payment.service.PaymentReader;
import io.hhplus.server.domain.reservation.entity.Reservation;
import io.hhplus.server.domain.reservation.repository.ReservationRepository;
import io.hhplus.server.domain.reservation.service.dto.OccupyTempReservationDto;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReservationMonitor {
    /* 예약 요청을 5분 동안 임시 점유하고, 5분 후 상태를 추적하여 점유 해제 처리하는 모니터링 클래스 */

    private final ReservationRepository reservationRepository;
    private final PaymentReader paymentReader;
    private final ConcertService concertService;
    private final RedissonClient redissonClient;

    // Redisson 지연 큐 선언
    private RBlockingQueue<OccupyTempReservationDto> tempReservationQueue;
    private RDelayedQueue<OccupyTempReservationDto> delayedReservationQueue;

    @PostConstruct
    public void init() {
        // Redisson 큐 초기화
        tempReservationQueue = redissonClient.getBlockingQueue("tempReservationQueue");
        delayedReservationQueue = redissonClient.getDelayedQueue(tempReservationQueue);
    }

    public void occupyReservation(Long reservationId) {
        // 예약 요청을 지연 큐에 추가, 5분 후에 처리되도록 설정
        OccupyTempReservationDto occupyDto = OccupyTempReservationDto.toOccupy(reservationId);
        delayedReservationQueue.offer(occupyDto, 5, TimeUnit.MINUTES);
    }

    @PreDestroy
    public void destroy() {
        delayedReservationQueue.destroy();
    }

    public void reservationMonitoring() {
        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    // 요소가 있을 때까지 블록
                    OccupyTempReservationDto occupyDto = tempReservationQueue.take();
                    // 예약, 결제 상태 확인 및 처리 로직
                    processReservation(occupyDto);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }).start();
    }

    private void processReservation(OccupyTempReservationDto occupyDto) {
        Reservation reservation = reservationRepository.findById(occupyDto.reservationId());
        Payment payment = paymentReader.findPaymentByReservation(reservation);
        if ((reservation != null && reservation.getStatus().equals(Reservation.Status.ING)) && (payment == null)) {
            // 임시 점유 해제: 완료되지 않은 예약 취소
            concertService.patchSeatStatus(reservation.getConcertDateId(), reservation.getSeatNum(), Seat.Status.AVAILABLE);
            reservationRepository.delete(reservation);
            log.info("완료되지 않은 예약 취소: {}", occupyDto.reservationId());
        }
    }
}
