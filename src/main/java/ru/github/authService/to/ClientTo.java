package ru.github.authService.to;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClientTo {
    @NotBlank
    @Size(min = 2, max = 128)
    String login;

    @NotBlank
    @Size(min = 5, max = 128)
    String password;
}
