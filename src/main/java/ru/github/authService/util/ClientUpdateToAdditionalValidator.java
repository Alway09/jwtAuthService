package ru.github.authService.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import ru.github.authService.AuthClient;
import ru.github.authService.model.Client;
import ru.github.authService.repository.ClientRepository;
import ru.github.authService.to.ClientUpdateInfoTo;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ClientUpdateToAdditionalValidator implements org.springframework.validation.Validator {
    private final ClientRepository repository;

    @Override
    public boolean supports(Class<?> clazz) {
        return ClientUpdateInfoTo.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ClientUpdateInfoTo clientUpdateInfoTo = (ClientUpdateInfoTo) target;
        final String newLog = clientUpdateInfoTo.getNewLogin();

        Optional<Client> dbClient = repository.findClientByLoginIgnoreCase(newLog);
        if (dbClient.isPresent() && !dbClient.get().equals(AuthClient.get().getClient())) {
            errors.rejectValue("newLogin", "", "Client with login '" + newLog + "' already exists");
        }
    }
}
