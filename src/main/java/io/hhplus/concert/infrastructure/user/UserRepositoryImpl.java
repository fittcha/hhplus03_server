package io.hhplus.concert.infrastructure.user;

import io.hhplus.concert.domain.user.entity.Users;
import io.hhplus.concert.domain.user.repository.UserJpaRepository;
import io.hhplus.concert.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.repository.query.Param;
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
    public Users findByIdWithPessimisticLock(@Param("userId") Long userId) {
        return userJpaRepository.findByIdWithPessimisticLock(userId);
    }

    @Override
    public void save(Users users) {
        userJpaRepository.save(users);
    }
}
