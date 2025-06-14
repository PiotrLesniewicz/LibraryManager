package org.library.domain.exception;

import jakarta.persistence.EntityNotFoundException;

public class NotFoundUserException extends EntityNotFoundException {
    public NotFoundUserException(String message) {
        super(message);
    }
}
