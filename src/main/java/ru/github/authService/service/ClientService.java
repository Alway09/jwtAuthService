package ru.github.authService.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.DisabledException;
import org.springframework.stereotype.Service;
import ru.github.authService.repository.ClientRepository;
import ru.github.authService.model.Client;

import static ru.github.authService.config.SecurityConfig.PASSWORD_ENCODER;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientService {
    private final ClientRepository clientRepository;
    private final AuthService authService;

    public Client register(String login, String password) {
        log.info("Register client login '" + login + "'");
        Client client = new Client(null, login, password);
        client = clientRepository.prepareAndSave(client);
        log.info("Client login '" + login + "' registered");
        return client;
    }

    public Client update(Client client, String newLogin, String newPassword) {
        log.info("Update client id '" + client.getId() + "'");

        if (!newLogin.trim().equals("")) {
            authService.logout(client); // Необходимо т.к. рефреш токен генерируется с содержанием логина
            client.setLogin(newLogin.toLowerCase());
        }
        if (!newPassword.trim().equals("")) {
            client.setPassword(PASSWORD_ENCODER.encode(newPassword));
        }

        client = clientRepository.save(client);
        log.info("Client id '" + client.getId() + "' updated");
        return client;
    }

    public Client enable(String login) {
        log.info("Enabling client login '" + login + "'");
        Client client = clientRepository.getExisted(login);
        client.setEnabled(true);
        clientRepository.save(client);
        log.info("Client login '" + login + "' enabled");
        return client;
    }

    public Client disable(Client client) {
        log.info("Disabling serve client login '" + client.getLogin() + "'");
        if (!client.isEnabled()) {
            throw new DisabledException("Client already disabled");
        }
        client.setEnabled(false);
        clientRepository.save(client);
        authService.logout(client);
        log.info("Client login '" + client.getLogin() + "' serve disabled");
        return client;
    }
}
