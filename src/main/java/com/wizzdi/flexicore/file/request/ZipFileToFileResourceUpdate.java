package com.wizzdi.flexicore.file.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.flexicore.file.model.ZipFileToFileResource;

public class ZipFileToFileResourceUpdate extends ZipFileToFileResourceCreate {

	private String id;
	@JsonIgnore
	private ZipFileToFileResource zipFileToFileResource;

	public String getId() {
		return id;
	}

	public <T extends ZipFileToFileResourceUpdate> T setId(String id) {
		this.id = id;
		return (T) this;
	}

	@JsonIgnore
	public ZipFileToFileResource getZipFileToFileResource() {
		return zipFileToFileResource;
	}

	public <T extends ZipFileToFileResourceUpdate> T setZipFileToFileResource(ZipFileToFileResource zipFileToFileResource) {
		this.zipFileToFileResource = zipFileToFileResource;
		return (T) this;
	}
}
