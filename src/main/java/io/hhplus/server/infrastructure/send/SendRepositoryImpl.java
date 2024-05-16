package io.hhplus.server.infrastructure.send;

import io.hhplus.server.domain.send.entity.Send;
import io.hhplus.server.domain.send.repository.SendJpaRepository;
import io.hhplus.server.domain.send.repository.SendRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SendRepositoryImpl implements SendRepository {

    private final SendJpaRepository sendJpaRepository;

    public SendRepositoryImpl(SendJpaRepository sendJpaRepository) {
        this.sendJpaRepository = sendJpaRepository;
    }

    @Override
    public Send save(Send send) {
        return sendJpaRepository.save(send);
    }

    @Override
    public Send findById(Long sendId) {
        return sendJpaRepository.findById(sendId).orElse(null);
    }

    @Override
    public List<Send> findAllByStatus(Send.Status status) {
        return sendJpaRepository.findAllByStatus(status);
    }
}
