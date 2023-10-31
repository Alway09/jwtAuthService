package ru.github.authService.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = "basicAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "basic"
)
@SecurityScheme(
        name = "bearer-key",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
@OpenAPIDefinition(
        info = @Info(
                title = "REST API documentation",
                version = "1.0",
                description = """
                        Микросервис "Клиенты" по <a href='https://sberuniversity.online/programs/20443/about?is_iframe_mode=1'>курсу Школа Java Developer</a>: решение выпускного проекта
                        <p><b>Тестовые креденшелы:</b><br>
                        - valentine / password<br>
                        - dimitrius / newpass<br>
                        - disabledclient / newpass</p>
                        """,
                contact = @Contact(
                        name = "Чекулаев Валентин",
                        email = "alvvay09@gmail.com"
                )
        ),
        security = {@SecurityRequirement(name = "basicAuth"), @SecurityRequirement(name = "bearer-key")}
)
public class OpenApiConfig {
    @Bean
    public GroupedOpenApi api() {
        return GroupedOpenApi.builder()
                .group("REST API")
                .pathsToMatch("/api/**")
                .build();
    }
}
