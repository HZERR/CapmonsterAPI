package ru.hzerr.capmonster;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import ru.hzerr.capmonster.exception.CapmonsterConnectionException;
import ru.hzerr.capmonster.exception.CapmonsterConnectionIOException;
import ru.hzerr.capmonster.exception.CapmonsterFailedOperationException;
import ru.hzerr.capmonster.exception.CapmonsterInterruptedOperationException;
import ru.hzerr.capmonster.request.CreateTaskRequest;
import ru.hzerr.capmonster.request.GetBalanceRequest;
import ru.hzerr.capmonster.request.GetTaskResultRequest;
import ru.hzerr.capmonster.response.ErrorData;
import ru.hzerr.capmonster.response.Response;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

public abstract class AbstractCapmonster implements ICapmonster {

    private final String clientKey;

    private final Gson gson = new GsonBuilder().setPrettyPrinting().setLenient().create();
    private final HttpClient CLIENT = HttpClient.newHttpClient();

    public AbstractCapmonster(String clientKey) {
        this.clientKey = clientKey;
    }

    /**
     * @return taskId
     */
    protected <T> int createTask(T request) throws CapmonsterInterruptedOperationException, CapmonsterConnectionIOException, CapmonsterConnectionException, CapmonsterFailedOperationException {
        CreateTaskRequest<T> createTaskRequest = new CreateTaskRequest<>();
        createTaskRequest.setData(request);
        createTaskRequest.setClientKey(clientKey);

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://api.capmonster.cloud/createTask"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(createTaskRequest)))
                .build();

        HttpResponse<String> response;

        try {
            response = CLIENT.send(httpRequest, BodyHandlers.ofString());
        } catch (InterruptedException ie) {
            throw new CapmonsterInterruptedOperationException(ie.getMessage());
        } catch (IOException io) {
            throw new CapmonsterConnectionIOException(io.getMessage(), io);
        }

        if (response.statusCode() == 200) {
            JsonObject responseAsObject = gson.fromJson(response.body(), JsonElement.class).getAsJsonObject();
            if (responseAsObject.get("errorId").getAsInt() == 0) {
                return responseAsObject.get("taskId").getAsInt();
            } else
                throw new CapmonsterFailedOperationException("Exception code: " + responseAsObject.get("errorCode").getAsString());
        }

        throw new CapmonsterConnectionException("Invalid status code: " + response.statusCode());
    }

    protected <T> Response<T> getTaskResult(Class<T> responseDataType, int taskId) throws CapmonsterInterruptedOperationException, CapmonsterConnectionIOException, CapmonsterFailedOperationException, CapmonsterConnectionException {
        HttpRequest getTaskResultRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://api.capmonster.cloud/getTaskResult"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(GetTaskResultRequest.from(clientKey, taskId))))
                .build();

        HttpResponse<String> response;

        try {
            response = CLIENT.send(getTaskResultRequest, BodyHandlers.ofString());
        } catch (InterruptedException ie) {
            throw new CapmonsterInterruptedOperationException(ie.getMessage());
        } catch (IOException io) {
            throw new CapmonsterConnectionIOException(io.getMessage(), io);
        }

        if (response.statusCode() == 200) {
            JsonObject responseAsObject = gson.fromJson(response.body(), JsonElement.class).getAsJsonObject();
            if (responseAsObject.get("status").getAsString().equalsIgnoreCase("processing")) {
                return Response.processing();
            }

            if (responseAsObject.get("errorId").getAsInt() == 0) {
                return Response.success(gson.fromJson(responseAsObject.get("solution"), responseDataType));
            } else
                return Response.failure(ErrorData.from(responseAsObject.get("errorCode").getAsString(), responseAsObject.get("errorDescription").getAsString()));
        }

        throw new CapmonsterConnectionException("Invalid status code: " + response.statusCode());
    }

    public BigDecimal getBalance() throws CapmonsterInterruptedOperationException, CapmonsterConnectionIOException, CapmonsterConnectionException, CapmonsterFailedOperationException {
        HttpRequest getBalanceRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://api.capmonster.cloud/getBalance"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(GetBalanceRequest.from(clientKey))))
                .build();

        HttpResponse<String> response;

        try {
            response = CLIENT.send(getBalanceRequest, BodyHandlers.ofString());
        } catch (InterruptedException ie) {
            throw new CapmonsterInterruptedOperationException(ie.getMessage());
        } catch (IOException io) {
            throw new CapmonsterConnectionIOException(io.getMessage(), io);
        }

        if (response.statusCode() == 200) {
            JsonObject responseAsObject = gson.fromJson(response.body(), JsonElement.class).getAsJsonObject();
            if (responseAsObject.get("errorId").getAsInt() == 0) {
                return responseAsObject.get("balance").getAsBigDecimal();
            } else
                throw new CapmonsterFailedOperationException("Exception code: " + responseAsObject.get("errorCode").getAsString());
        }

        throw new CapmonsterConnectionException("Invalid status code: " + response.statusCode());
    }

    @Override
    public void close() {
        CLIENT.close();
    }
}
