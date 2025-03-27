package org.library.domain.exception;

public class LibrarianMissingException extends UserValidationException
{
    public LibrarianMissingException(String message) {
        super(message);
    }
}
