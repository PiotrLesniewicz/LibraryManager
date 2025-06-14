package org.library.domain.exception;

import jakarta.persistence.EntityNotFoundException;

public class NotFoundLibrarianException extends EntityNotFoundException {
    public NotFoundLibrarianException(String message) {
        super(message);
    }
}
