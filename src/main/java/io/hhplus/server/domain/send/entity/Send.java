package io.hhplus.server.domain.send.entity;

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
public class Send extends BaseDateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sendId;

    @Column(nullable = false)
    private Type type;

    @Column(nullable = false)
    private Status status;

    private String jsonData;

    @Builder
    public Send(Long sendId, Type type, Status status, String jsonData) {
        this.sendId = sendId;
        this.type = type;
        this.status = status;
        this.jsonData = jsonData;
    }

    public static Send toEntity(Type type, Status status, String jsonData) {
        return Send.builder()
                .type(type)
                .status(status)
                .jsonData(jsonData)
                .build();
    }

    public void updateStatus(Status status) {
        this.status = status;
    }

    public enum Type {
        RESERVATION
    }

    public enum Status {
        READY,
        ING,
        DONE
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Send send = (Send) o;
        return Objects.equals(sendId, send.sendId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(sendId);
    }
}
