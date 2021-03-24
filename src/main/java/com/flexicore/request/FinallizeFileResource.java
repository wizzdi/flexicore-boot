package com.flexicore.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.flexicore.file.model.FileResource;

public class FinallizeFileResource {
    private String fileResourceId;
    @JsonIgnore
    private FileResource fileResource;

    public String getFileResourceId() {
        return fileResourceId;
    }

    public FinallizeFileResource setFileResourceId(String fileResourceId) {
        this.fileResourceId = fileResourceId;
        return this;
    }

    @JsonIgnore
    public FileResource getFileResource() {
        return fileResource;
    }

    public FinallizeFileResource setFileResource(FileResource fileResource) {
        this.fileResource = fileResource;
        return this;
    }
}
