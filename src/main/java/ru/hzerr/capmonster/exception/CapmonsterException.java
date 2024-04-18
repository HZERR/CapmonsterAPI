package ru.hzerr.capmonster.exception;

public class CapmonsterException extends Exception {

    public CapmonsterException(String message) {
        super(message);
    }

    public CapmonsterException(String message, Exception cause) {
        super(message, cause);
    }
}
