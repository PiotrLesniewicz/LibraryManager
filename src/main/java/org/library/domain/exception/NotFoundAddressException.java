package org.library.domain.exception;

import jakarta.persistence.EntityNotFoundException;

public class NotFoundAddressException extends EntityNotFoundException {
    public NotFoundAddressException(String message) {
        super(message);
    }
}
