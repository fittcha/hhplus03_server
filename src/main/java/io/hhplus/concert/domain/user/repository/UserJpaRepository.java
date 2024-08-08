package io.hhplus.concert.domain.user.repository;

import io.hhplus.concert.domain.user.entity.Users;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserJpaRepository extends JpaRepository<Users, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select u from Users u where u.userId = :userId")
    Users findByIdWithPessimisticLock(@Param("userId") Long userId);
}
