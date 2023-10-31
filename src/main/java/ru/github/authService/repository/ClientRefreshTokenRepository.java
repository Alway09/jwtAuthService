package ru.github.authService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.github.authService.model.ClientRefreshToken;

@Transactional(readOnly = true)
public interface ClientRefreshTokenRepository extends JpaRepository<ClientRefreshToken, Integer> {
}
