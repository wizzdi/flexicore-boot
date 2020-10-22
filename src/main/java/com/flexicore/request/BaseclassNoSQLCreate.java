package com.flexicore.request;

public class BaseclassNoSQLCreate {

    private String name;

    public String getName() {
        return name;
    }

    public <T extends BaseclassNoSQLCreate> T setName(String name) {
        this.name = name;
        return (T) this;
    }
}
