package io.hhplus.concert.base.exception;

import io.hhplus.concert.base.enums.MessageCommInterface;

import java.io.Serializable;

public record ApiResult<T>(
        boolean success,
        T data,
        Error error
) implements Serializable {

    public static <T> ApiResult<T> successNoContent() {
        return new ApiResult<>(true, null, null);
    }

    public static <T> ApiResult<T> success(T data) {
        return new ApiResult<>(true, data, null);
    }

    public static <T> ApiResult<T> fail(MessageCommInterface messageCommInterface) {
        return new ApiResult<>(false, null, new Error(messageCommInterface.getCode(), messageCommInterface.getMessage()));
    }

    public static <T> ApiResult<T> error(String errorCode, String message, T data) {
        return new ApiResult<>(false, data, new Error(errorCode, message));
    }

    public static <T> ApiResult<T> error(String message) {
        return new ApiResult<>(false, null, new Error(null, message));
    }

    public record Error(
            String errorCode,
            String message
    ) {
    }
}
