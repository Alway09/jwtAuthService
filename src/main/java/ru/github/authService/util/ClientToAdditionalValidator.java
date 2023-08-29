package ru.github.authService.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import ru.github.authService.model.Client;
import ru.github.authService.repository.ClientRepository;
import ru.github.authService.to.ClientTo;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ClientToAdditionalValidator implements org.springframework.validation.Validator {
    private final ClientRepository repository;
    private final HttpServletRequest request;
    @Override
    public boolean supports(Class<?> clazz) {
        return ClientTo.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ClientTo clientTo = (ClientTo) target;
        if (StringUtils.hasText(clientTo.getLogin())) {
            Optional<Client> dbClient = repository.findClientByLoginIgnoreCase(clientTo.getLogin());
            if (dbClient.isPresent()) {
                Assert.notNull(request, "HttpServletRequest missed");

                if (request.getRequestURI().endsWith("/signup") && dbClient.get().isEnabled()) {
                    errors.rejectValue("login", "", "Client with login '" + clientTo.getLogin() + "' already exist and enabled");
                }

                if (request.getRequestURI().endsWith("/signin") && !dbClient.get().isEnabled()) {
                    errors.rejectValue("login", "", "Client with login '" + clientTo.getLogin() + "' disabled");
                }

                if (request.getRequestURI().endsWith("/enable") && dbClient.get().isEnabled()) {
                    errors.rejectValue("login", "", "Client with login '" + clientTo.getLogin() + "' already enabled");
                }
            }
        }
    }
}
