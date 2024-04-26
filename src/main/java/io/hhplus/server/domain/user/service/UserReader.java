package io.hhplus.server.domain.user.service;

import io.hhplus.server.domain.user.entity.Users;
import io.hhplus.server.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserReader {
    /* User 관련 정보 단순 조회용 */

    private final UserRepository userRepository;

    public Users findUser(Long userId) {
        return userRepository.findById(userId);
    }
}
