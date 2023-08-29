package ru.github.authService.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.github.authService.model.Client;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenUtil {
    public final long jwtAccessTokenValidityMilliseconds;
    public final Long jwtRefreshTokenValidityMilliseconds;
    private final SecretKey jwtAccessSecret;
    private final SecretKey jwtRefreshSecret;

    public JwtTokenUtil(
            @Value("${jwt.secret.access}") String jwtAccessSecret,
            @Value("${jwt.secret.refresh}") String jwtRefreshSecret,
            @Value("${jwt.validity.duration.access}") String accessTokenValidityMinutes,
            @Value("${jwt.validity.duration.refresh}") String refreshTokenValidityDays
    ) {
        this.jwtAccessSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtAccessSecret));
        this.jwtRefreshSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtRefreshSecret));
        this.jwtAccessTokenValidityMilliseconds = Duration.ofMinutes(Long.parseLong(accessTokenValidityMinutes)).toMillis();
        this.jwtRefreshTokenValidityMilliseconds = Duration.ofDays(Long.parseLong(refreshTokenValidityDays)).toMillis();
    }

    public String getAccessSubject(String accessToken) {
        return getClaims(accessToken, jwtAccessSecret).getSubject();
    }

    public String getRefreshSubject(String refreshToken) {
        return getClaims(refreshToken, jwtRefreshSecret).getSubject();
    }

    public String generateAccess(Client client) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerate(claims, client.getId().toString(), jwtAccessSecret, jwtAccessTokenValidityMilliseconds);
    }

    public String generateRefresh(Client client) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerate(claims, client.getLogin(), jwtRefreshSecret, jwtRefreshTokenValidityMilliseconds);
    }

    public void validateAccess(String accessToken) {
        validate(accessToken, jwtAccessSecret);
    }

    public void validateRefresh(String refreshToken) {
        validate(refreshToken, jwtRefreshSecret);
    }

    private String doGenerate(Map<String, Object> claims, String subject, Key secret, Long expirationTimeMs) {
        final long currentTimeMilliseconds = System.currentTimeMillis();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(currentTimeMilliseconds))
                .setExpiration(new Date(currentTimeMilliseconds + expirationTimeMs * 1000))
                .signWith(secret)
                .compact();
    }

    private Claims getClaims(String token, Key secret) {
        return Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private void validate(String token, Key secret) {
        Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token);
    }
}
