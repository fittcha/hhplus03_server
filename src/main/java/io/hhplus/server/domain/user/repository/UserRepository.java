package io.hhplus.server.domain.user.repository;

import io.hhplus.server.domain.user.entity.Users;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository {

    Users findById(Long userId);

    Users findByIdWithPessimisticLock(@Param("userId") Long userId);

    void save(Users users);
}
