package io.hhplus.concert.base.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class SpringConfig {

    @Value("${spring.session.redis.namespace}")
    private String sessionRedisNamespace;

    @Value("${spring.profiles.active}")
    private String activeProfile;
}
