package io.hhplus.server.domain.send.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SendEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void send(SendEvent event) {
        applicationEventPublisher.publishEvent(event);
    }
}
