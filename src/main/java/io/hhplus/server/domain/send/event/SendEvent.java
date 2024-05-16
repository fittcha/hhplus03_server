package io.hhplus.server.domain.send.event;

import io.hhplus.server.domain.send.dto.SendCommReqDto;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class SendEvent extends ApplicationEvent {

    private final SendCommReqDto sendReqDto;

    public SendEvent(Object source, SendCommReqDto sendReqDto) {
        super(source);
        this.sendReqDto = sendReqDto;
    }
}
