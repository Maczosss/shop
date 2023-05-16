package org.application.exception;

public class ClientRepositoryException extends RuntimeException {
    public ClientRepositoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
