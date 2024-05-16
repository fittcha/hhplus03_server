package io.hhplus.server.domain.send.repository;

import io.hhplus.server.domain.send.entity.Send;
import org.springframework.stereotype.Repository;

@Repository
public interface SendRepository {

    Send save(Send send);

    Send findById(Long sendId);
}
