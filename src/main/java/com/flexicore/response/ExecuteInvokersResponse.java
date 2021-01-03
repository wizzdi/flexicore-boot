package com.flexicore.response;

import java.util.List;

public class ExecuteInvokersResponse {
    private List<ExecuteInvokerResponse<?>> responses;

    public ExecuteInvokersResponse(List<ExecuteInvokerResponse<?>> responses) {
        this.responses = responses;
    }

    public List<ExecuteInvokerResponse<?>> getResponses() {
        return responses;
    }

    public ExecuteInvokersResponse setResponses(List<ExecuteInvokerResponse<?>> responses) {
        this.responses = responses;
        return this;
    }
}
