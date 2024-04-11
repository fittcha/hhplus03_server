package io.hhplus.server.base.exception;

import io.hhplus.server.base.enums.MessageCommInterface;

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

    public record Error(
            String errorCode,
            String message
    ) {
    }
}
