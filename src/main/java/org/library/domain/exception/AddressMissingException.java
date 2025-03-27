package org.library.domain.exception;

public class AddressMissingException extends UserValidationException {
    public AddressMissingException(String message) {
        super(message);
    }
}
