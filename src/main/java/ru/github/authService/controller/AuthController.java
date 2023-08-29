package ru.github.authService.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import ru.github.authService.AuthClient;
import ru.github.authService.service.AuthService;
import ru.github.authService.to.ClientTo;
import ru.github.authService.to.JwtResponse;
import ru.github.authService.to.RefreshJwtRequest;
import ru.github.authService.util.ClientToAdditionalValidator;

@RestController
@RequestMapping(value = AuthController.URL, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    public static final String URL = "/api/v1/auth";
    private final AuthService authService;
    private final ClientToAdditionalValidator clientToAdditionalValidator;

    @InitBinder("clientTo")
    protected void initClientToBinder(WebDataBinder binder) {
        binder.addValidators(clientToAdditionalValidator);
    }

    @Operation(summary = "Аутентификация")
    @PostMapping(value = "/signin")
    public JwtResponse authenticateClient(@RequestBody @Valid ClientTo clientTo) {
        authService.authClient(clientTo.getLogin(), clientTo.getPassword());
        JwtResponse response = authService.authenticate(AuthClient.authClient());
        authService.unauthClient();
        return response;
    }

    @Operation(summary = "Выход из системы", security = {@SecurityRequirement(name = "bearer-key")})
    @PostMapping(value = "/signout")
    public void unauthenticateClient() {
        authService.logout(AuthClient.authClient());
    }

    @Operation(summary = "Получение нового access токена")
    @PostMapping("/token")
    public JwtResponse getNewAccessToken(@RequestBody RefreshJwtRequest request) {
        return authService.getAccessToken(request.getRefreshToken());
    }

    @Operation(summary = "Получение нового refresh токена", security = {@SecurityRequirement(name = "bearer-key")},
            description = "И refresh, и access токены должны быть валидными")
    @PostMapping(value = "/refresh")
    public JwtResponse getNewRefreshToken(@RequestBody RefreshJwtRequest request) {
        return authService.refresh(request.getRefreshToken(), AuthClient.authClient());
    }
}
