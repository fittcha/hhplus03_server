package io.hhplus.concert.base.exception;

import io.hhplus.concert.base.enums.MessageCommInterface;
import lombok.Getter;
import org.springframework.boot.logging.LogLevel;

@Getter
public class CustomException extends RuntimeException {
    private final String errorCode;
    private final String message;
    private final Object data;
    private final LogLevel logLevel;

    public CustomException(MessageCommInterface messageCommInterface, Object data, LogLevel logLevel) {
        super(messageCommInterface.getMessage());
        this.errorCode = messageCommInterface.getCode();
        this.message = messageCommInterface.getMessage();
        this.data = data;
        this.logLevel = logLevel;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
