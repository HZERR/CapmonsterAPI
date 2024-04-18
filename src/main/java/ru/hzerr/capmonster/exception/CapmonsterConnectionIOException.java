package ru.hzerr.capmonster.exception;

import java.io.IOException;

public class CapmonsterConnectionIOException extends CapmonsterException {

    public CapmonsterConnectionIOException(String message, IOException cause) {
        super(message, cause);
    }
}
