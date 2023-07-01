package com.wizzdi.dynamic.properties.converter;

public class JsonConverterImplementationHolder {
    private final JsonConverterImplementation jsonConverterImplementation;

    public JsonConverterImplementationHolder(JsonConverterImplementation jsonConverterImplementation) {
        this.jsonConverterImplementation = jsonConverterImplementation;
    }

    public JsonConverterImplementation getJsonConverterImplementation() {
        return jsonConverterImplementation;
    }
}
