package com.flexicore.request;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class DynamicExecutionExampleRequest {

    private String id;
    @JsonIgnore
    private String className;

    public String getId() {
        return id;
    }

    public <T extends DynamicExecutionExampleRequest> T setId(String id) {
        this.id = id;
        return (T) this;
    }

    @JsonIgnore
    public String getClassName() {
        return className;
    }

    public <T extends DynamicExecutionExampleRequest> T setClassName(String className) {
        this.className = className;
        return (T) this;
    }
}
