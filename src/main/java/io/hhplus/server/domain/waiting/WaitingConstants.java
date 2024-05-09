package io.hhplus.server.domain.waiting;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WaitingConstants {

    // key 패턴
    // {{환경 설정 파일}}:hhplus-waiting:{{KEY}}
    public static final String WAIT_KEY = "waiting:wait";
    public static final String ACTIVE_KEY = "waiting:active";

    public static final int MAX_ACTIVE_USER = 2000;
    public static final int ENTER_10_SECONDS = 300;
    public static final long AUTO_EXPIRED_TIME = 10 * 60 * 1000;
}
