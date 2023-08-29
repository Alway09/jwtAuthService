package ru.github.authService.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "refresh_token", uniqueConstraints = {@UniqueConstraint(columnNames = {"refresh_token", "client_login"})})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClientRefreshToken {
    @Id
    @Column(name = "client_login")
    private String clientLogin;

    @Column(name = "refresh_token", nullable = false)
    private String refreshToken;

    public ClientRefreshToken(String clientLogin, String refreshToken) {
        this.clientLogin = clientLogin;
        this.refreshToken = refreshToken;
    }
}
