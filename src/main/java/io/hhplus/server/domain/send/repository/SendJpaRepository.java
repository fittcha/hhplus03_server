package io.hhplus.server.domain.send.repository;

import io.hhplus.server.domain.send.entity.Send;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SendJpaRepository extends JpaRepository<Send, Long> {

    List<Send> findAllByStatus(Send.Status status);
}
