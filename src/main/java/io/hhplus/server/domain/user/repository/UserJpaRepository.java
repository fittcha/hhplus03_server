package io.hhplus.server.domain.user.repository;

import io.hhplus.server.domain.user.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<Users, Long> {
}
