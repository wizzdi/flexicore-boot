/*******************************************************************************
 *  Copyright (C) FlexiCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Avishay Ben Natan And Asaf Ben Natan, October 2015
 ******************************************************************************/
package com.flexicore.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.flexicore.annotations.AnnotatedClazz;
import com.wizzdi.flexicore.boot.jaxrs.annotations.Views;
import com.flexicore.security.SecurityContext;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
@AnnotatedClazz(Category = "FileResouce", Name = "FileResourceInterface", Description = "Used to track file based resources")
@Entity

@Table(indexes = {@Index(name = "fileResource_md5_ix", columnList = "md5")})
public class FileResource extends Baseclass {
    private String md5;
    @Column(name = "fileoffset")
    private long offset;
    private String actualFilename;
    private String originalFilename;
    private boolean done;
    private String path;

    @Column(columnDefinition = "timestamp with time zone")
    private OffsetDateTime dateTaken;
    private boolean nonDownloadable;


    @OneToMany(targetEntity = ZipFileToFileResource.class, mappedBy = "zippedFile")
    @JsonIgnore
    private List<ZipFileToFileResource> zipFileToFileResources = new ArrayList<>();
    @Column(columnDefinition = "timestamp with time zone")
    private OffsetDateTime keepUntil;
    @Lob
    private String onlyFrom;

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public String getActualFilename() {
        return actualFilename;
    }

    public void setActualFilename(String actualFilename) {
        this.actualFilename = actualFilename;
    }


    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public FileResource() {
        super();
        offset = 0;
    }

    public FileResource(String name, SecurityContext securityContext) {
        super(name, securityContext);
    }

    @JsonView(Views.Elaborative.class)
    public String getFullPath() {
        return path;
    }

    public void setFullPath(String fullPath) {
        path = fullPath;
    }


    public boolean isNonDownloadable() {
        return nonDownloadable;
    }

    public void setNonDownloadable(boolean nonDownloadable) {
        this.nonDownloadable = nonDownloadable;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public OffsetDateTime getDateTaken() {
        return dateTaken;
    }

    public void setDateTaken(OffsetDateTime dateTaken) {
        this.dateTaken = dateTaken;
    }

    @Temporal(TemporalType.DATE)
    public OffsetDateTime getKeepUntil() {
        return keepUntil;
    }

    public void setKeepUntil(OffsetDateTime keepUntil) {
        this.keepUntil = keepUntil;
    }

    @OneToMany(targetEntity = ZipFileToFileResource.class, mappedBy = "zippedFile")
    @JsonIgnore
    public List<ZipFileToFileResource> getZipFileToFileResources() {
        return zipFileToFileResources;
    }

    public <T extends FileResource> T setZipFileToFileResources(List<ZipFileToFileResource> zipFileToFileResources) {
        this.zipFileToFileResources = zipFileToFileResources;
        return (T) this;
    }

    @Lob
    public String getOnlyFrom() {
        return onlyFrom;
    }

    public <T extends FileResource> T setOnlyFrom(String onlyFrom) {
        this.onlyFrom = onlyFrom;
        return (T) this;
    }

    @Override
    public String toString() {
        return "FileResource{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
