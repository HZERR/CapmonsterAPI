package ru.hzerr.capmonster;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import ru.hzerr.capmonster.exception.CapmonsterCancelledOperationException;
import ru.hzerr.capmonster.exception.CapmonsterConnectionException;
import ru.hzerr.capmonster.exception.CapmonsterConnectionIOException;
import ru.hzerr.capmonster.exception.CapmonsterFailedOperationException;
import ru.hzerr.capmonster.request.GetBalanceRequest;
import ru.hzerr.capmonster.request.ImageToTextRequest;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;

public abstract class AbstractCapmonster implements ICapmonster, AutoCloseable {

    private final String clientKey;

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final HttpClient CLIENT = HttpClient.newHttpClient();

    public AbstractCapmonster(String clientKey) {
        this.clientKey = clientKey;
    }

    /**
     * @return taskId
     */
    protected int createTask(ImageToTextRequest request) throws CapmonsterCancelledOperationException, CapmonsterConnectionIOException, CapmonsterConnectionException, CapmonsterFailedOperationException {
        HttpRequest createTaskRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://api.capmonster.cloud/createTask"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(request)))
                .build();

        HttpResponse<String> response;

        try {
            response = CLIENT.send(createTaskRequest, HttpResponse.BodyHandlers.ofString(Charset.defaultCharset()));
        } catch (InterruptedException ie) {
            throw new CapmonsterCancelledOperationException(ie.getMessage());
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

    public BigDecimal getBalance() throws CapmonsterCancelledOperationException, CapmonsterConnectionIOException, CapmonsterConnectionException, CapmonsterFailedOperationException {
        HttpRequest getBalanceRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://api.capmonster.cloud/getBalance"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(GetBalanceRequest.from(clientKey))))
                .build();

        HttpResponse<String> response;

        try {
            response = CLIENT.send(getBalanceRequest, HttpResponse.BodyHandlers.ofString());
        } catch (InterruptedException ie) {
            throw new CapmonsterCancelledOperationException(ie.getMessage());
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
