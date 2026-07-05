package dev.shop.backend.exceptions;

import org.springframework.security.authentication.BadCredentialsException;

public class InvalidProductOwnerException extends BadCredentialsException {
    public InvalidProductOwnerException(String message) {
        super(message);
    }
}
