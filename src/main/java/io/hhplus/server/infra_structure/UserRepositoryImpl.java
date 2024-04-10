package io.hhplus.server.infra_structure;

import io.hhplus.server.domain.user.repository.UserJpaRepository;
import io.hhplus.server.domain.user.repository.UserRepository;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    public UserRepositoryImpl(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }
}
