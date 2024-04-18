package ru.hzerr.capmonster.response;

public interface IReadOnlyResponse<T> {

    ErrorData getErrorData();
    Status getStatus();
    T getData();
}
