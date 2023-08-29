package ru.github.authService.error;

public class NotFoundException extends AppException {
    public NotFoundException(String message) {
        super(message);
    }
}
