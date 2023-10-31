package ru.github.authService.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = HealthCheckController.URL)
public class HealthCheckController {
    public static final String URL = "/api/v1/auth/health";

    @Operation(summary = "Проверка статуса работы сервиса")
    @GetMapping
    public String getHealthStatus() {
        return "OK";
    }
}
