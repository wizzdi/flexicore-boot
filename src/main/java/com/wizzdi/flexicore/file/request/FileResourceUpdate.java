package com.wizzdi.flexicore.file.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.flexicore.file.model.FileResource;

public class FileResourceUpdate extends FileResourceCreate {

	private String id;
	@JsonIgnore
	private FileResource fileResource;

	public String getId() {
		return id;
	}

	public <T extends FileResourceUpdate> T setId(String id) {
		this.id = id;
		return (T) this;
	}

	@JsonIgnore
	public FileResource getFileResource() {
		return fileResource;
	}

	public <T extends FileResourceUpdate> T setFileResource(FileResource fileResource) {
		this.fileResource = fileResource;
		return (T) this;
	}
}
