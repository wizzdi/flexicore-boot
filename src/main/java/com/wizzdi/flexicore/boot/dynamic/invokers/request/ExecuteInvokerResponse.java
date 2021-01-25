package com.wizzdi.flexicore.boot.dynamic.invokers.request;

public class ExecuteInvokerResponse<E> {
  private boolean executed;
  private E response;
  private String invokerName;

    public ExecuteInvokerResponse(String invokerName, boolean executed, E response) {
        this.executed = executed;
        this.response = response;
        this.invokerName=invokerName;
    }

    public ExecuteInvokerResponse() {
    }

    public boolean isExecuted() {
        return executed;
    }

    public ExecuteInvokerResponse<E> setExecuted(boolean executed) {
        this.executed = executed;
        return this;
    }

    public E getResponse() {
        return response;
    }

    public ExecuteInvokerResponse<E> setResponse(E response) {
        this.response = response;
        return this;
    }

    public String getInvokerName() {
        return invokerName;
    }

    public ExecuteInvokerResponse<E> setInvokerName(String invokerName) {
        this.invokerName = invokerName;
        return this;
    }

    @Override
    public String toString() {
        return "ExecuteInvokerResponse{" +
                "executed=" + executed +
                ", response=" + response +
                ", invokerName='" + invokerName + '\'' +
                '}';
    }
}
