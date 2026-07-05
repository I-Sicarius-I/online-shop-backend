package dev.shop.backend.exceptions;

import org.springframework.security.authentication.BadCredentialsException;

public class InvalidReviewOwnerException extends BadCredentialsException {
    public InvalidReviewOwnerException(String message) {
        super(message);
    }
}
