package com.cuecolab.cuecolab.backend.exceptions;

public class NoUserFoundInTheDatabaseException extends RuntimeException {
    public NoUserFoundInTheDatabaseException() {
        super();
    }

    public NoUserFoundInTheDatabaseException(String message) {
        super(message);
    }

    public NoUserFoundInTheDatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
