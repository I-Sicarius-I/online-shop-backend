package dev.shop.backend.exceptions;

public class ProductOwnerSelfOrderException extends RuntimeException {
    public ProductOwnerSelfOrderException(String message) {
        super(message);
    }
}
