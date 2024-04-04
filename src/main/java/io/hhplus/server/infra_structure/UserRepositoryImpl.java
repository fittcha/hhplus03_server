package io.hhplus.server.infra_structure;

import io.hhplus.server.domain.user.repository.UserJpaRepository;
import io.hhplus.server.domain.user.repository.UserRepository;

public class UserRepositoryImpl implements UserRepository {

    private UserJpaRepository userJpaRepository;
}
