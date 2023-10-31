package ru.github.authService.service;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.github.authService.model.ClientRefreshToken;
import ru.github.authService.repository.ClientRefreshTokenRepository;
import ru.github.authService.repository.ClientRepository;
import ru.github.authService.util.JwtTokenUtil;
import ru.github.authService.model.Client;
import ru.github.authService.to.JwtResponse;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {
    private final ClientRepository clientRepository;
    private final ClientRefreshTokenRepository tokenRepository;
    private final JwtTokenUtil jwtTokenUtil;

    public JwtResponse getNewAccessToken(String refreshToken) {
        log.info("Getting new access token");
        Integer clientId = validateRefreshToken(refreshToken).getClientId();
        Client client = clientRepository.getExisted(clientId);
        return new JwtResponse(jwtTokenUtil.generateAccess(client), refreshToken);
    }

    public JwtResponse refresh(String refreshToken, Client client) {
        log.info("Refreshing tokens");
        ClientRefreshToken dbRefreshToken = validateRefreshToken(refreshToken);
        JwtResponse response = generateTokensPair(client);
        dbRefreshToken.setRefreshToken(response.getRefreshToken());
        return response;
    }

    public JwtResponse authenticate(Client client) {
        log.info("Authenticating client login '" + client.getLogin() + "'");

        JwtResponse response = generateTokensPair(client);
        tokenRepository.save(new ClientRefreshToken(client.getId(), response.getRefreshToken()));
        return response;
    }

    public void logout(Client client) {
        log.info("Client login '" + client.getLogin() + "' logout");
        tokenRepository.deleteById(client.getId());
    }

    private ClientRefreshToken validateRefreshToken(String refreshToken) {
        jwtTokenUtil.validateRefresh(refreshToken);
        Integer clientId = Integer.parseInt(jwtTokenUtil.getRefreshSubject(refreshToken));
        Optional<ClientRefreshToken> dbRefreshToken = tokenRepository.findById(clientId);
        if (dbRefreshToken.isEmpty()) {
            throw new JwtException("Refresh token not found");
        }
        return dbRefreshToken.get();
    }

    private JwtResponse generateTokensPair(Client client) {
        return new JwtResponse(
                jwtTokenUtil.generateAccess(client),
                jwtTokenUtil.generateRefresh(client));
    }
}
