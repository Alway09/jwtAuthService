package ru.github.authService.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import ru.github.authService.AuthClient;
import ru.github.authService.service.AuthService;
import ru.github.authService.service.ClientService;
import ru.github.authService.to.ClientTo;
import ru.github.authService.to.ClientUpdateTo;
import ru.github.authService.util.ClientToAdditionalValidator;
import ru.github.authService.util.ClientUpdateToAdditionalValidator;

@RestController
@RequestMapping(value = ClientController.URL, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
public class ClientController {
    public static final String URL = "/api/v1/client";
    private final ClientService clientService;
    private final AuthService authService;
    private final ClientToAdditionalValidator clientToAdditionalValidator;
    private final ClientUpdateToAdditionalValidator clientUpdateToAdditionalValidator;

    @InitBinder("clientTo")
    protected void initClientToBinder(WebDataBinder binder) {
        binder.addValidators(clientToAdditionalValidator);
    }

    @InitBinder("clientUpdateTo")
    protected void initClientUpdateToBinder(WebDataBinder binder) {
        binder.addValidators(clientUpdateToAdditionalValidator);
    }

    @Operation(summary = "Регистрация")
    @PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE)
    @SuppressWarnings("rawtypes")
    public ResponseEntity registerClient(@RequestBody @Valid ClientTo clientTo) {
        clientService.register(clientTo.getLogin(), clientTo.getPassword());
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @Operation(summary = "Обновление данных", security = {@SecurityRequirement(name = "bearer-key")},
    description = "При обновлении логина необходимо заново пройти аутентификацию, т.к. refresh токен инвалидируется")
    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(@RequestBody @Valid ClientUpdateTo clientUpdateTo) {
        clientService.update(AuthClient.authClient(), clientUpdateTo.getNewLogin(), clientUpdateTo.getNewPassword());
        authService.unauthClient();
    }

    @Operation(summary = "Возобновление клиента")
    @PatchMapping(value = "/enable", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void enableServe(@RequestBody @Valid ClientTo clientTo) {
        authService.authClient(clientTo.getLogin(), clientTo.getPassword());
        clientService.enable(AuthClient.authClient().getLogin());
        authService.unauthClient();
    }

    @Operation(summary = "Остановка обслуживания", security = {@SecurityRequirement(name = "bearer-key")})
    @PatchMapping(value = "/disable")
    public void disableServe() {
        clientService.disable(AuthClient.authClient());
        authService.unauthClient();
    }
}
