package io.hhplus.concert.domain.user.service;

import io.hhplus.concert.domain.user.entity.Users;
import io.hhplus.concert.domain.user.repository.UserRepository;
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
