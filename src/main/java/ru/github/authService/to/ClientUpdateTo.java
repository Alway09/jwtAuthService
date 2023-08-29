package ru.github.authService.to;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ClientUpdateTo {
    @NotNull
    String newLogin;

    @NotNull
    String newPassword;
}
