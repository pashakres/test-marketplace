package com.challenge.marketplace.exceptions;

public class PurchaseException extends Exception {
    public PurchaseException(String message) {
        super(message);
    }
    public PurchaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
