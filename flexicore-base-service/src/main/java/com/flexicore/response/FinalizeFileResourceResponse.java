package com.flexicore.response;

import com.wizzdi.flexicore.file.model.FileResource;

public class FinalizeFileResourceResponse {

    private FileResource fileResource;
    private String md5;
    private String expectedMd5;
    private boolean finalized;

    public FileResource getFileResource() {
        return fileResource;
    }

    public FinalizeFileResourceResponse setFileResource(FileResource fileResource) {
        this.fileResource = fileResource;
        return this;
    }

    public String getMd5() {
        return md5;
    }

    public FinalizeFileResourceResponse setMd5(String md5) {
        this.md5 = md5;
        return this;
    }

    public String getExpectedMd5() {
        return expectedMd5;
    }

    public FinalizeFileResourceResponse setExpectedMd5(String expectedMd5) {
        this.expectedMd5 = expectedMd5;
        return this;
    }

    public boolean isFinalized() {
        return finalized;
    }

    public FinalizeFileResourceResponse setFinalized(boolean finalized) {
        this.finalized = finalized;
        return this;
    }
}
