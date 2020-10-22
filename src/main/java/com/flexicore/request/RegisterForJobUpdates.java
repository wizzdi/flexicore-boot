package com.flexicore.request;

public class RegisterForJobUpdates {

    private String jobId;
    private String webSocketSessionId;


    public String getJobId() {
        return jobId;
    }

    public RegisterForJobUpdates setJobId(String jobId) {
        this.jobId = jobId;
        return this;
    }

    public String getWebSocketSessionId() {
        return webSocketSessionId;
    }

    public RegisterForJobUpdates setWebSocketSessionId(String webSocketSessionId) {
        this.webSocketSessionId = webSocketSessionId;
        return this;
    }
}
