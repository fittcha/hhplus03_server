package io.hhplus.concert.domain.user.repository;

import io.hhplus.concert.domain.user.entity.Users;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository {

    Users findById(Long userId);

    Users findByIdWithPessimisticLock(@Param("userId") Long userId);

    void save(Users users);
}
