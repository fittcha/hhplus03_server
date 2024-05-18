package io.hhplus.server.domain.outbox.entity;

import io.hhplus.server.base.entity.BaseDateTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
public class Outbox extends BaseDateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long outboxId;

    @Column(nullable = false)
    private Type type;

    @Column(nullable = false)
    private Status status;

    private String jsonData;

    @Builder
    public Outbox(Long outboxId, Type type, Status status, String jsonData) {
        this.outboxId = outboxId;
        this.type = type;
        this.status = status;
        this.jsonData = jsonData;
    }

    public static Outbox toEntity(Type type, Status status, String jsonData) {
        return Outbox.builder()
                .type(type)
                .status(status)
                .jsonData(jsonData)
                .build();
    }

    public void updateStatus(Status status) {
        this.status = status;
    }

    public enum Type {
        RESERVE,
        CANCEL
    }

    public enum Status {
        INIT,
        DONE
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Outbox outbox = (Outbox) o;
        return Objects.equals(outboxId, outbox.outboxId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(outboxId);
    }
}
