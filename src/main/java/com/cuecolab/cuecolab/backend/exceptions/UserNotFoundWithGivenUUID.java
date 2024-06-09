package com.cuecolab.cuecolab.backend.exceptions;

public class UserNotFoundWithGivenUUID extends RuntimeException {
    public UserNotFoundWithGivenUUID() {
    }

    public UserNotFoundWithGivenUUID(String message) {
        super(message);
    }

    public UserNotFoundWithGivenUUID(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNotFoundWithGivenUUID(Throwable cause) {
        super(cause);
    }

    public UserNotFoundWithGivenUUID(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
