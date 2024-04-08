package io.hhplus.server.controller.concert.dto.response;

import io.hhplus.server.domain.concert.entity.Concert;
import io.hhplus.server.domain.concert.entity.ConcertDate;
import io.hhplus.server.domain.concert.entity.Place;
import io.hhplus.server.domain.concert.entity.Seat;
import lombok.Builder;

import java.text.DecimalFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

public record GetConcertResponse(
        Long concertId,
        String name,
        String hall,
        String period,
        String price,
        ZonedDateTime createdAt
) {
    @Builder
    public GetConcertResponse {
    }

    public static GetConcertResponse from(Concert concert, Place place) {
        return GetConcertResponse.builder()
                .concertId(concert.getConcertId())
                .name(concert.getName())
                .hall(place != null ? place.getName() : null)
                .period(getConcertDateRange(concert.getConcertDateList()))
                .price(place != null ? getSeatPriceRange(place.getSeatList()) : null)
                .createdAt(concert.getCreatedAt())
                .build();
    }

    private static String getConcertDateRange(List<ConcertDate> concertDateList) {
        // 콘서트 날짜 범위 문자열로 반환
        if (concertDateList.isEmpty()) {
            return "";
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");

        concertDateList.sort(Comparator.comparing(ConcertDate::getConcertDate));
        String earliestDate = formatter.format(concertDateList.get(0).getConcertDate());
        String latestDate = formatter.format(concertDateList.get(concertDateList.size() - 1).getConcertDate());

        return earliestDate + "~" + latestDate;
    }

    private static String getSeatPriceRange(List<Seat> seatList) {
        // 좌석 가격 범위 문자열로 반환
        if (seatList.isEmpty()) {
            return "";
        }

        DecimalFormat formatter = new DecimalFormat("###,###원");

        seatList.sort(Comparator.comparing(Seat::getPrice));
        String lowestPrice = formatter.format(seatList.get(0).getPrice());
        String largestPrice = formatter.format(seatList.get(seatList.size() - 1).getPrice());

        return lowestPrice + "~" + largestPrice;
    }
}
