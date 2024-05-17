package io.hhplus.server.domain.send.event;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SendEvent {

    private Long outboxId;
    private String jsonData;

    public SendEvent(Long outboxId, String jsonData) {
        this.outboxId = outboxId;
        this.jsonData = jsonData;
    }
}
