package io.hhplus.server.domain.send.repository;

import io.hhplus.server.domain.send.entity.Send;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SendJpaRepository extends JpaRepository<Send, Long> {

}
