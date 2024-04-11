package io.hhplus.server.base.exception;

import io.hhplus.server.base.enums.MessageCommInterface;
import lombok.Getter;

public class CustomException extends RuntimeException {
    @Getter
    private final String errorCode;
    private final String message;

    public CustomException(MessageCommInterface messageCommInterface) {
        super(messageCommInterface.getMessage());
        this.errorCode = messageCommInterface.getCode();
        this.message = messageCommInterface.getMessage();
    }

    @Override
    public String getMessage() {
        return message;
    }
}
