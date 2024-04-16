package io.hhplus.server.infrastructure.user;

import io.hhplus.server.domain.user.entity.User;
import io.hhplus.server.domain.user.repository.UserJpaRepository;
import io.hhplus.server.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    public UserRepositoryImpl(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public User findById(Long userId) {
        return userJpaRepository.findById(userId).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public void save(User user) {
        userJpaRepository.save(user);
    }
}
