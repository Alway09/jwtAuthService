package ru.github.authService.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import ru.github.authService.model.Client;
import ru.github.authService.repository.ClientRepository;
import ru.github.authService.to.ClientUpdateTo;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ClientUpdateToAdditionalValidator implements org.springframework.validation.Validator {
    private final ClientRepository repository;
    private final HttpServletRequest request;

    @Override
    public boolean supports(Class<?> clazz) {
        return ClientUpdateTo.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        final int loginLengthMin = 3;
        final int loginLengthMax = 128;
        final int passwordLengthMin = 6;
        final int passwordLengthMax = 128;

        ClientUpdateTo clientUpdateTo = (ClientUpdateTo) target;
        final String newLog = clientUpdateTo.getNewLogin();
        final String newPass = clientUpdateTo.getNewPassword();

        if (!StringUtils.hasText(newLog) && !StringUtils.hasText(newPass)) {
            errors.rejectValue("newLogin", "", "New credentials is not present. Client not updated");
            errors.rejectValue("newPassword", "", "New credentials is not present. Client not updated");
        }

        if (StringUtils.hasText(newLog) && (newLog.length() < loginLengthMin || newLog.length() > loginLengthMax)) {
            errors.rejectValue("newLogin", "", "New login length must be between " +
                    loginLengthMin + " and " +
                    loginLengthMax);
        }

        if (StringUtils.hasText(newPass) && (newPass.length() < passwordLengthMin || newPass.length() > passwordLengthMax)) {
            errors.rejectValue("newPassword", "", "New password length must be between " +
                    passwordLengthMin + " and " +
                    passwordLengthMax);
        }

        Optional<Client> dbClient = repository.findClientByLoginIgnoreCase(newLog);
        if (dbClient.isPresent()) {
            errors.rejectValue("newLogin", "", "Client with login '" + newLog + "' already exists");
        }
    }
}
