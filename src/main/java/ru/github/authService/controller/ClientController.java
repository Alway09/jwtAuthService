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
import ru.github.authService.service.ClientService;
import ru.github.authService.to.ClientChangePasswordTo;
import ru.github.authService.to.ClientTo;
import ru.github.authService.to.ClientUpdateInfoTo;
import ru.github.authService.util.ClientToAdditionalValidator;
import ru.github.authService.util.ClientUpdateToAdditionalValidator;

@RestController
@RequestMapping(value = ClientController.URL, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
public class ClientController {
    public static final String URL = "/api/v1/client";
    private final ClientService clientService;
    private final ClientToAdditionalValidator clientToAdditionalValidator;
    private final ClientUpdateToAdditionalValidator clientUpdateToAdditionalValidator;

    @InitBinder("clientTo")
    protected void initClientToBinder(WebDataBinder binder) {
        binder.addValidators(clientToAdditionalValidator);
    }

    @InitBinder("clientUpdateInfoTo")
    protected void initClientUpdateToBinder(WebDataBinder binder) {
        binder.addValidators(clientUpdateToAdditionalValidator);
    }

    @Operation(summary = "Регистрация клиента")
    @PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE)
    @SuppressWarnings("rawtypes")
    public ResponseEntity registerClient(@RequestBody @Valid ClientTo clientTo) {
        clientService.register(clientTo);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @Operation(summary = "Обновление данных клиента", security = {@SecurityRequirement(name = "bearer-key")})
    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(@RequestBody @Valid ClientUpdateInfoTo clientUpdateInfoTo) {
        clientService.updateInfo(AuthClient.authClient(), clientUpdateInfoTo);
    }

    @Operation(summary = "Смена пароля клиента", security = {@SecurityRequirement(name = "bearer-key")})
    @PutMapping(value = "/changePass", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void changePassword(@RequestBody @Valid ClientChangePasswordTo changePasswordTo) {
        clientService.changePassword(AuthClient.authClient(), changePasswordTo);
    }

    @Operation(summary = "Возобновление обслуживания клиента", security = {@SecurityRequirement(name = "basicAuth")})
    @PatchMapping(value = "/enable")
    public void enableServe() {
        clientService.enable(AuthClient.authClient());
    }

    @Operation(summary = "Остановка обслуживания клиента", security = {@SecurityRequirement(name = "bearer-key")})
    @PatchMapping(value = "/disable")
    public void disableServe() {
        clientService.disable(AuthClient.authClient());
    }
}
