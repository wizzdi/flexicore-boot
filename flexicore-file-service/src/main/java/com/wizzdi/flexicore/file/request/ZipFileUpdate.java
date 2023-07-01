package com.wizzdi.flexicore.file.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wizzdi.flexicore.file.model.ZipFile;

public class ZipFileUpdate extends ZipFileCreate {

	private String id;
	@JsonIgnore
	private ZipFile zipFile;

	public String getId() {
		return id;
	}

	public <T extends ZipFileUpdate> T setId(String id) {
		this.id = id;
		return (T) this;
	}

	@JsonIgnore
	public ZipFile getZipFile() {
		return zipFile;
	}

	public <T extends ZipFileUpdate> T setZipFile(ZipFile zipFile) {
		this.zipFile = zipFile;
		return (T) this;
	}
}
