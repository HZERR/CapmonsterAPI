package ru.hzerr.capmonster.request;

public class GetTaskResultRequest {

    private final String clientKey;
    private final int taskId;

    private GetTaskResultRequest(String clientKey, int taskId) {
        this.clientKey = clientKey;
        this.taskId = taskId;
    }

    public String getClientKey() {
        return clientKey;
    }

    public int getTaskId() {
        return taskId;
    }

    public static GetTaskResultRequest from(String clientKey, int taskId) {
        return new GetTaskResultRequest(clientKey, taskId);
    }
}
