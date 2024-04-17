package io.hhplus.server.domain.waiting.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
@Table(name = "waiting_queue")
public class WaitingQueue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long waitingQueueId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    private Timestamp activeTime;

    private Timestamp requestTime = new Timestamp(System.currentTimeMillis());

    public static WaitingQueue toActiveEntity(Long userId, String token) {
        return WaitingQueue.builder()
                .userId(userId)
                .token(token)
                .status(Status.ACTIVE)
                .activeTime(new Timestamp(System.currentTimeMillis()))
                .build();
    }

    public static WaitingQueue toWaitingEntity(Long userId, String token) {
        return WaitingQueue.builder()
                .userId(userId)
                .token(token)
                .status(Status.WAITING)
                .build();
    }

    public void expiredToken() {
        this.status = Status.EXPIRED;
    }

    public void toActive() {
        this.status = Status.ACTIVE;
    }

    public enum Status {
        EXPIRED,
        ACTIVE,
        WAITING
    }

    @Builder
    public WaitingQueue(Long waitingQueueId, Long userId, String token, Status status, Timestamp activeTime, Timestamp requestTime) {
        this.waitingQueueId = waitingQueueId;
        this.userId = userId;
        this.token = token;
        this.status = status;
        this.activeTime = activeTime;
        this.requestTime = requestTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WaitingQueue that = (WaitingQueue) o;
        return Objects.equals(waitingQueueId, that.waitingQueueId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(waitingQueueId);
    }
}
