package ru.github.authService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.github.authService.error.NotFoundException;
import ru.github.authService.model.Client;

import java.util.Optional;

import static ru.github.authService.config.SecurityConfig.PASSWORD_ENCODER;

@Transactional(readOnly = true)
public interface ClientRepository extends JpaRepository<Client, Integer> {
    @Transactional
    default Client prepareAndSave(Client client) {
        client.setPassword(PASSWORD_ENCODER.encode(client.getPassword()));
        client.setLogin(client.getLogin().toLowerCase());
        return save(client);
    }

    default Client getExisted(Integer id) {
        return findById(id).orElseThrow(() -> new NotFoundException("Client id '" + id + "' not found"));
    }

    default Client getExisted(String login) {
        return findClientByLoginIgnoreCase(login).orElseThrow(() -> new NotFoundException("Client login '" + login + "' not found"));
    }

    Optional<Client> findClientByLoginIgnoreCase(String login);
}
