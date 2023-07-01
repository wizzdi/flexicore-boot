package com.wizzdi.flexicore.file.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.flexicore.file.model.FileResource;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ZipAndDownloadRequest {

    private Set<String> fileResourceIds=new HashSet<>();
    @JsonIgnore
    private List<FileResource> fileResources;
    private boolean failOnMissing;
    private long offset;

    public Set<String> getFileResourceIds() {
        return fileResourceIds;
    }

    public <T extends ZipAndDownloadRequest> T setFileResourceIds(Set<String> fileResourceIds) {
        this.fileResourceIds = fileResourceIds;
        return (T) this;
    }

    @JsonIgnore
    public List<FileResource> getFileResources() {
        return fileResources;
    }

    public <T extends ZipAndDownloadRequest> T setFileResources(List<FileResource> fileResources) {
        this.fileResources = fileResources;
        return (T) this;
    }

    public boolean isFailOnMissing() {
        return failOnMissing;
    }

    public <T extends ZipAndDownloadRequest> T setFailOnMissing(boolean failOnMissing) {
        this.failOnMissing = failOnMissing;
        return (T) this;
    }

    public long getOffset() {
        return offset;
    }

    public <T extends ZipAndDownloadRequest> T setOffset(long offset) {
        this.offset = offset;
        return (T) this;
    }
}
