package io.hhplus.concert;

import lombok.Getter;
import org.springframework.stereotype.Service;

import java.time.Clock;

@Getter
@Service
public class TimeService {

    private Clock clock = Clock.systemDefaultZone();

    public void setClock(Clock clock) {
        this.clock = clock;
    }
}
