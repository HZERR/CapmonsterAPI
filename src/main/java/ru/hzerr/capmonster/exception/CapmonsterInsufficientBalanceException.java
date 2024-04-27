package ru.hzerr.capmonster.exception;

public class CapmonsterInsufficientBalanceException extends CapmonsterException {
    public CapmonsterInsufficientBalanceException(String message) {
        super(message);
    }

    public CapmonsterInsufficientBalanceException(String message, Exception cause) {
        super(message, cause);
    }
}
