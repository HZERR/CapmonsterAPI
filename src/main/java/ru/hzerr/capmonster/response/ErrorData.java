package ru.hzerr.capmonster.response;

public class ErrorData {

    private final String code;
    private final String description;

    private ErrorData(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static ErrorData from(String code, String description) {
        return new ErrorData(code, description);
    }
}
