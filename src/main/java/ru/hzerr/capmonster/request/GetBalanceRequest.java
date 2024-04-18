package ru.hzerr.capmonster.request;

public class GetBalanceRequest {

    private final String clientKey;

    private GetBalanceRequest(String clientKey) {
        this.clientKey = clientKey;
    }

    public String getClientKey() {
        return clientKey;
    }

    public static GetBalanceRequest from(String clientKey) {
        return new GetBalanceRequest(clientKey);
    }
}
