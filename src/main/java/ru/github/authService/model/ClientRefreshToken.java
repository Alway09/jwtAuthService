package ru.github.authService.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "refresh_token", uniqueConstraints = {@UniqueConstraint(columnNames = {"refresh_token", "client_id"})})
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClientRefreshToken {
    @Id
    @Column(name = "client_id")
    private Integer clientId;

    @Column(name = "refresh_token", nullable = false)
    private String refreshToken;

    public ClientRefreshToken(Integer clientId, String refreshToken) {
        this.clientId = clientId;
        this.refreshToken = refreshToken;
    }
}
