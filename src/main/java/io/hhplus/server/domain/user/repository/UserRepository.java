package io.hhplus.server.domain.user.repository;

import io.hhplus.server.domain.user.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository {

    User findById(Long userId);

    void save(User user);
}
