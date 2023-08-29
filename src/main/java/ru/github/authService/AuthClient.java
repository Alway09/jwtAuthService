package ru.github.authService;

import lombok.Getter;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import ru.github.authService.model.Client;

import java.util.Collections;

import static java.util.Objects.requireNonNull;

@Getter
public class AuthClient extends User {
    private final Client client;

    public AuthClient(@NonNull Client client) {
        super(client.getLogin(), client.getPassword(), Collections.EMPTY_SET);
        this.client = client;
    }

    public static AuthClient safeGet() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return null;
        }
        return (auth.getPrincipal() instanceof AuthClient ac) ? ac : null;
    }

    public static AuthClient get() {
        return requireNonNull(safeGet(), "No authorized client found");
    }

    public static Client authClient() {
        return get().getClient();
    }
}
