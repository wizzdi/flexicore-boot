package com.wizzdi.flexicore.file.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ZipFile extends FileResource {


    private String uniqueFilesMd5;

    public ZipFile() {
    }

    @JsonIgnore
    @OneToMany(targetEntity = ZipFileToFileResource.class,mappedBy = "zipFile")
    private List<ZipFileToFileResource> zippedFilesToFileResourceList=new ArrayList<>();

    @JsonIgnore
    @OneToMany(targetEntity = ZipFileToFileResource.class,mappedBy = "zipFile")
    public List<ZipFileToFileResource> getZippedFilesToFileResourceList() {
        return zippedFilesToFileResourceList;
    }

    public <T extends ZipFile> T setZippedFilesToFileResourceList(List<ZipFileToFileResource> zippedFilesToFileResourceList) {
        this.zippedFilesToFileResourceList = zippedFilesToFileResourceList;
        return (T) this;
    }

    public String getUniqueFilesMd5() {
        return uniqueFilesMd5;
    }

    public <T extends ZipFile> T setUniqueFilesMd5(String uniqueFilesMd5) {
        this.uniqueFilesMd5 = uniqueFilesMd5;
        return (T) this;
    }
}
