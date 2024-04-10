package io.hhplus.server.domain.user.service;

import io.hhplus.server.domain.user.entity.User;
import io.hhplus.server.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserReader {
    /* 타 도메인에서 User 관련 정보 단순 조회용 */

    private final UserRepository userRepository;

    public User getUser(Long userId) {
        return userRepository.findById(userId);
    }
}
