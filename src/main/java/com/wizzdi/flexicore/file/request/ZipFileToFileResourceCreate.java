package com.wizzdi.flexicore.file.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.flexicore.file.model.FileResource;
import com.wizzdi.flexicore.file.model.ZipFile;
import com.wizzdi.flexicore.security.request.BasicCreate;

public class ZipFileToFileResourceCreate extends BasicCreate {

	private String zipFileId;
	@JsonIgnore
	private ZipFile zipFile;
	private String fileResourceId;
	@JsonIgnore
	private FileResource fileResource;


	public String getZipFileId() {
		return zipFileId;
	}

	public <T extends ZipFileToFileResourceCreate> T setZipFileId(String zipFileId) {
		this.zipFileId = zipFileId;
		return (T) this;
	}

	@JsonIgnore
	public ZipFile getZipFile() {
		return zipFile;
	}

	public <T extends ZipFileToFileResourceCreate> T setZipFile(ZipFile zipFile) {
		this.zipFile = zipFile;
		return (T) this;
	}

	public String getFileResourceId() {
		return fileResourceId;
	}

	public <T extends ZipFileToFileResourceCreate> T setFileResourceId(String fileResourceId) {
		this.fileResourceId = fileResourceId;
		return (T) this;
	}

	@JsonIgnore
	public FileResource getFileResource() {
		return fileResource;
	}

	public <T extends ZipFileToFileResourceCreate> T setFileResource(FileResource fileResource) {
		this.fileResource = fileResource;
		return (T) this;
	}
}
