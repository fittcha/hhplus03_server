package io.hhplus.concert.base.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.hhplus.concert.base.adapter.ZonedDateTimeAdapter;

import java.time.ZonedDateTime;

public class JsonUtil {

    public static String toJson(Object obj) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeAdapter())
                .create();
        return gson.toJson(obj);
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeAdapter())
                .create();
        return gson.fromJson(json, clazz);
    }
}
