package com.flexicore.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.security.SecurityContext;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ZipFile extends FileResource {



    public ZipFile() {
    }

    public ZipFile(String name, SecurityContext securityContext) {
        super(name, securityContext);
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
}
