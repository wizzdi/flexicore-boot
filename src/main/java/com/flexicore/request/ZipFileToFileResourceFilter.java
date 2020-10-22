package com.flexicore.request;

import com.flexicore.model.FileResource;
import com.flexicore.model.FilteringInformationHolder;
import com.flexicore.model.ZipFile;

import java.util.List;

public class ZipFileToFileResourceFilter extends FilteringInformationHolder {

    private List<ZipFile> zipFiles;
    private List<FileResource> fileResources;

    public List<ZipFile> getZipFiles() {
        return zipFiles;
    }

    public <T extends ZipFileToFileResourceFilter> T setZipFiles(List<ZipFile> zipFiles) {
        this.zipFiles = zipFiles;
        return (T) this;
    }

    public List<FileResource> getFileResources() {
        return fileResources;
    }

    public <T extends ZipFileToFileResourceFilter> T setFileResources(List<FileResource> fileResources) {
        this.fileResources = fileResources;
        return (T) this;
    }
}
