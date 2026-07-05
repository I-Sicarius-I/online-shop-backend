package dev.shop.backend.exceptions;

import org.springframework.security.authentication.BadCredentialsException;

public class InvalidOrderOwnerException extends BadCredentialsException {
    public InvalidOrderOwnerException(String message) {
        super(message);
    }
}
