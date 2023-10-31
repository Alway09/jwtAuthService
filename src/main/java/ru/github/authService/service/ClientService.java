package ru.github.authService.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.DisabledException;
import org.springframework.stereotype.Service;
import ru.github.authService.repository.ClientRepository;
import ru.github.authService.model.Client;
import ru.github.authService.to.ClientChangePasswordTo;
import ru.github.authService.to.ClientTo;
import ru.github.authService.to.ClientUpdateInfoTo;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientService {
    private final ClientRepository clientRepository;
    private final AuthService authService;

    public Client register(ClientTo clientTo) {
        log.info("Register client login '" + clientTo.getLogin() + "'");
        Client client = new Client(null, clientTo.getLogin(), clientTo.getPassword());
        client = clientRepository.prepareAndSave(client);
        log.info("Client login '" + clientTo.getLogin() + "' registered");
        return client;
    }

    public Client updateInfo(Client client, ClientUpdateInfoTo updateTo) {
        log.info("Update info client id '" + client.getId() + "'");
        client.setLogin(updateTo.getNewLogin().toLowerCase());
        client = clientRepository.save(client);
        log.info("Client id '" + client.getId() + "' updated");
        return client;
    }

    public void changePassword(Client client, ClientChangePasswordTo changePasswordTo) {
        log.info("Changing password for client login '" + client.getLogin() + "'");
        client.setPassword(changePasswordTo.getNewPassword());
        clientRepository.prepareAndSave(client);
        log.info("Password changed for client login '" + client.getLogin() + "'");
    }

    public Client enable(Client client) {
        log.info("Enabling client login '" + client.getLogin() + "'");
        client.setEnabled(true);
        clientRepository.save(client);
        log.info("Client login '" + client.getLogin() + "' enabled");
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
