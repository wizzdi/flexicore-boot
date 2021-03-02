package com.flexicore.model;

import com.flexicore.security.SecurityContext;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class ZipFileToFileResource extends Baseclass {



    public ZipFileToFileResource() {
    }

    public ZipFileToFileResource(String name, SecurityContext securityContext) {
        super(name, securityContext);
    }

    @ManyToOne(targetEntity = ZipFile.class,cascade = CascadeType.MERGE)
    private ZipFile zipFile;

    @ManyToOne(targetEntity = FileResource.class,cascade = CascadeType.MERGE)
    private FileResource zippedFile;

    @ManyToOne(targetEntity = ZipFile.class,cascade = CascadeType.MERGE)
    public ZipFile getZipFile() {
        return zipFile;
    }

    public <T extends ZipFileToFileResource> T setZipFile(ZipFile zipFile) {
        this.zipFile = zipFile;
        return (T) this;
    }

    @ManyToOne(targetEntity = FileResource.class,cascade = CascadeType.MERGE)
    public FileResource getZippedFile() {
        return zippedFile;
    }

    public <T extends ZipFileToFileResource> T setZippedFile(FileResource zippedFile) {
        this.zippedFile = zippedFile;
        return (T) this;
    }
}
