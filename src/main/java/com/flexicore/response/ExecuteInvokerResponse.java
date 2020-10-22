package com.flexicore.response;

public class ExecuteInvokerResponse {
  private boolean executed;
  private Object response;
  private String invokerName;

    public ExecuteInvokerResponse(String invokerName, boolean executed, Object response) {
        this.executed = executed;
        this.response = response;
        this.invokerName=invokerName;
    }

    public boolean isExecuted() {
        return executed;
    }

    public ExecuteInvokerResponse setExecuted(boolean executed) {
        this.executed = executed;
        return this;
    }

    public Object getResponse() {
        return response;
    }

    public ExecuteInvokerResponse setResponse(Object response) {
        this.response = response;
        return this;
    }

    public String getInvokerName() {
        return invokerName;
    }

    public ExecuteInvokerResponse setInvokerName(String invokerName) {
        this.invokerName = invokerName;
        return this;
    }
}
