package com.flexicore.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.FileResource;

import java.util.Map;

public class ProcessFileResource {
    private String fileResourceId;
    @JsonIgnore
    private FileResource fileResource;
    private Map<String, String> properties;

    public String getFileResourceId() {
        return fileResourceId;
    }

    public ProcessFileResource setFileResourceId(String fileResourceId) {
        this.fileResourceId = fileResourceId;
        return this;
    }

    @JsonIgnore
    public FileResource getFileResource() {
        return fileResource;
    }

    public ProcessFileResource setFileResource(FileResource fileResource) {
        this.fileResource = fileResource;
        return this;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public ProcessFileResource setProperties(Map<String, String> properties) {
        this.properties = properties;
        return this;
    }
}
