package ru.github.authService.service;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.github.authService.AuthClient;
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
    private final AuthenticationManager authManager;

    public JwtResponse getAccessToken(String refreshToken) {
        log.info("Getting new access token");
        String clientLogin = validateRefreshToken(refreshToken).getClientLogin();
        Client client = clientRepository.getExisted(clientLogin);
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
        JwtResponse response = generateTokensPair(client);
        tokenRepository.save(new ClientRefreshToken(client.getLogin(), response.getRefreshToken()));
        return response;
    }

    public void logout(Client client) {
        tokenRepository.deleteById(client.getLogin());
    }

    private ClientRefreshToken validateRefreshToken(String refreshToken) {
        jwtTokenUtil.validateRefresh(refreshToken);
        String clientLogin = jwtTokenUtil.getRefreshSubject(refreshToken);
        Optional<ClientRefreshToken> dbRefreshToken = tokenRepository.findById(clientLogin);
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

    public void authClient(String login, String password) {
        log.info("Authenticate client login '" + login + "'");
        Authentication authentication = new UsernamePasswordAuthenticationToken(login, password);
        authentication = authManager.authenticate(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public void unauthClient() {
        log.info("Unauthenticate client login '" + AuthClient.authClient().getLogin() + "'");
        SecurityContextHolder.getContext().setAuthentication(null);
    }
}
