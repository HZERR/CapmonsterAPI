package ru.hzerr.capmonster.request;

import com.google.gson.annotations.SerializedName;

public class CreateTaskRequest<T> {

    @SerializedName("clientKey")
    private String clientKey;

    @SerializedName("task")
    private T data;

    @SerializedName("callbackUrl")
    private String callbackURL;

    public CreateTaskRequest() {
    }

    public String getClientKey() {
        return clientKey;
    }

    public void setClientKey(String clientKey) {
        this.clientKey = clientKey;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getCallbackURL() {
        return callbackURL;
    }

    public void setCallbackURL(String callbackURL) {
        this.callbackURL = callbackURL;
    }
}
