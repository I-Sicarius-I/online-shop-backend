package dev.shop.backend.exceptions;

import org.springframework.security.authentication.BadCredentialsException;

public class InvalidReviewerException extends BadCredentialsException {
    public InvalidReviewerException(String message) {
        super(message);
    }
}
