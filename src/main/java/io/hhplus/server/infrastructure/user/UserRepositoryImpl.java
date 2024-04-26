package io.hhplus.server.infrastructure.user;

import io.hhplus.server.domain.user.entity.Users;
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
    public Users findById(Long userId) {
        return userJpaRepository.findById(userId).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public void save(Users users) {
        userJpaRepository.save(users);
    }
}
