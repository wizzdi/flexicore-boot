package com.flexicore.request;

public class FieldProperties {

    private String name;
    private int ordinal;

    public FieldProperties() {
    }

    public FieldProperties(String name, int ordinal) {
        this.name = name;
        this.ordinal = ordinal;
    }

    public String getName() {
        return name;
    }

    public <T extends FieldProperties> T setName(String name) {
        this.name = name;
        return (T) this;
    }

    public int getOrdinal() {
        return ordinal;
    }

    public <T extends FieldProperties> T setOrdinal(int ordinal) {
        this.ordinal = ordinal;
        return (T) this;
    }
}
