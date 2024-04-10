package io.hhplus.server.domain.place.service;

import io.hhplus.server.domain.place.entity.Place;
import io.hhplus.server.domain.place.entity.Seat;

import java.util.List;

public interface PlaceInterface {

    // 공연장 좌석 조회
    List<Seat> getSeatsByPlace(Long placeId);

    // 공연장 조회
    Place getPlace(Long placeId);
}
