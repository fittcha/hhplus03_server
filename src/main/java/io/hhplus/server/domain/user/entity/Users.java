package io.hhplus.server.domain.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private BigDecimal balance;

    @Version
    private Long version;

    public Users(Long userId, BigDecimal balance) {
        this.userId = userId;
        this.balance = balance;
    }

    public Users useBalance(BigDecimal price) {
        this.balance = balance.subtract(price);
        return this;
    }

    public Users chargeBalance(BigDecimal amount) {
        this.balance = balance.add(amount);
        return this;
    }

    public void refundBalance(BigDecimal price) {
        this.balance = balance.add(price);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Users users = (Users) o;
        return Objects.equals(userId, users.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(userId);
    }
}
