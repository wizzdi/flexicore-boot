package com.wizzdi.flexicore.file.request;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ZipFileCreate extends FileResourceCreate {

    @JsonIgnore
    private String uniqueFilesMd5;

    @JsonIgnore
    public String getUniqueFilesMd5() {
        return uniqueFilesMd5;
    }

    public <T extends ZipFileCreate> T setUniqueFilesMd5(String uniqueFilesMd5) {
        this.uniqueFilesMd5 = uniqueFilesMd5;
        return (T) this;
    }
}
