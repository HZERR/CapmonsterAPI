package ru.hzerr.capmonster.response;

public class Response<T> implements IReadOnlyResponse<T> {

    private final ErrorData errorData;
    private final Status status;
    private final T data;

    private Response(ErrorData errorData, Status status, T data) {
        this.errorData = errorData;
        this.status = status;
        this.data = data;
    }

    @Override
    public ErrorData getErrorData() {
        return errorData;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public T getData() {
        return data;
    }

    public static <T> Response<T> success(T data) {
        return new Response<>(null, Status.SUCCESS, data);
    }

    public static <T> Response<T> processing() {
        return new Response<>(null, Status.PROCESSING, null);
    }

    public static <T> Response<T> failure(ErrorData errorData) {
        return new Response<>(errorData, Status.FAILURE, null);
    }
}
