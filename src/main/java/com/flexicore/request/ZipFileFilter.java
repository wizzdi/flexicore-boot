package com.flexicore.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.flexicore.file.model.FileResource;

import java.util.List;
import java.util.Set;

public class ZipFileFilter extends FileResourceFilter {

    @JsonIgnore
    private List<FileResource> fileResources;
    private Set<String> fileResourcesIds;

    @JsonIgnore
    public List<FileResource> getFileResources() {
        return fileResources;
    }

    public <T extends ZipFileFilter> T setFileResources(List<FileResource> fileResources) {
        this.fileResources = fileResources;
        return (T) this;
    }

    public Set<String> getFileResourcesIds() {
        return fileResourcesIds;
    }

    public <T extends ZipFileFilter> T setFileResourcesIds(Set<String> fileResourcesIds) {
        this.fileResourcesIds = fileResourcesIds;
        return (T) this;
    }
}
