package ru.github.authService.to;

import lombok.Data;

@Data
public class JwtResponse {
    final String type = "Bearer";
    String accessToken;
    String refreshToken;

    public JwtResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
