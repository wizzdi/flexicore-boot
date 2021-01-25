package com.wizzdi.flexicore.boot.dynamic.invokers.request;

import java.util.List;

public class ExecuteInvokersResponse {
    private List<ExecuteInvokerResponse<?>> responses;

    public ExecuteInvokersResponse() {
    }

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

    @Override
    public String toString() {
        return "ExecuteInvokersResponse{" +
                "responses=" + responses +
                '}';
    }
}
