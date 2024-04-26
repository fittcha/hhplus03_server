package io.hhplus.server.intergration.base;

import io.hhplus.server.domain.concert.entity.Concert;
import io.hhplus.server.domain.concert.entity.ConcertDate;
import io.hhplus.server.domain.concert.entity.Place;
import io.hhplus.server.domain.concert.entity.Seat;
import io.hhplus.server.domain.concert.repository.ConcertRepository;
import io.hhplus.server.domain.concert.repository.PlaceRepository;
import io.hhplus.server.domain.concert.repository.SeatJpaRepository;
import io.hhplus.server.domain.payment.entity.Payment;
import io.hhplus.server.domain.payment.repository.PaymentRepository;
import io.hhplus.server.domain.reservation.entity.Reservation;
import io.hhplus.server.domain.reservation.repository.ReservationRepository;
import io.hhplus.server.domain.user.entity.User;
import io.hhplus.server.domain.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TestDataHandler {

    private final ConcertRepository concertRepository;
    private final PlaceRepository placeRepository;
    private final ReservationRepository reservationRepository;
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;

    private final EntityManager entityManager;
    private final SeatJpaRepository seatJpaRepository;

    // 콘서트 정보 세팅
    public void settingConcertInfo() {
        int seatsCnt = 50;
        Place place = Place.builder()
                .name("서울 올림픽경기장")
                .seatsCnt(seatsCnt)
                .build();
        placeRepository.addPlace(place);

        List<ConcertDate> concertDates = new ArrayList<>();
        concertDates.add(ConcertDate.builder()
                .concertDate(ZonedDateTime.of(LocalDateTime.of(2024, 5, 25, 18, 30), ZoneId.of("Asia/Seoul")))
                .build());
        concertDates.add(ConcertDate.builder()
                .concertDate(ZonedDateTime.of(LocalDateTime.of(2024, 5, 26, 19, 30), ZoneId.of("Asia/Seoul")))
                .build());

        concertRepository.addConcert(Concert.builder()
                .name("아이유 2024 콘서트")
                .place(place)
                .concertDateList(concertDates)
                .build());
        concertRepository.addConcertDates(concertDates);
        ConcertDate concertDate = entityManager.find(ConcertDate.class, 1L);

        List<Seat> seats = new ArrayList<>();
        for (int i = 1; i <= seatsCnt; i++) {
            if (i <= 20) {
                seats.add(Seat.builder().concertDate(concertDate).seatNum(i).price(BigDecimal.valueOf(89000)).status(Seat.Status.AVAILABLE).build());
            } else if (i <= 40) {
                seats.add(Seat.builder().concertDate(concertDate).seatNum(i).price(BigDecimal.valueOf(119000)).status(Seat.Status.AVAILABLE).build());
            } else {
                seats.add(Seat.builder().concertDate(concertDate).seatNum(i).price(BigDecimal.valueOf(139000)).status(Seat.Status.AVAILABLE).build());
            }
        }
        concertRepository.addSeats(seats);
    }

    // 5, 10번 좌석 예약
    @Transactional
    public void reserveSeats() {
        reservationRepository.save(Reservation.builder()
                .concertId(1L)
                .concertDateId(1L)
                .seatNum(5)
                .userId(1L)
                .status(Reservation.Status.ING)
                .reservedAt(ZonedDateTime.now().minusMinutes(3)).build());
        Seat seat5 = concertRepository.findSeatByConcertDateIdAndSeatNum(1L, 5);
        seat5.patchStatus(Seat.Status.DISABLE);

        reservationRepository.save(Reservation.builder()
                .concertId(1L)
                .concertDateId(1L)
                .seatNum(10)
                .userId(1L)
                .status(Reservation.Status.RESERVED)
                .reservedAt(ZonedDateTime.now().minusHours(1)).build());
        Seat seat10 = concertRepository.findSeatByConcertDateIdAndSeatNum(1L, 10);
        seat10.patchStatus(Seat.Status.DISABLE);

        entityManager.flush();
    }

    // 결제 건 생성
    public void createPayment(Payment.Status status) {
        Reservation reservation = entityManager.find(Reservation.class, 1L);

        paymentRepository.save(Payment.builder()
                .reservation(reservation)
                .price(BigDecimal.valueOf(89000))
                .status(status).build());
    }

    // 유저, 잔액 세팅
    public void settingUser(BigDecimal balance) {
        userRepository.save(new User(null, balance));
    }
}
