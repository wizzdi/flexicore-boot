package com.wizzdi.flexicore.file.request;

import com.wizzdi.flexicore.security.request.BasicCreate;

public class FileResourceCreate extends BasicCreate {

	private String md5;
	private String fullPath;
	private String actualFilename;
	private String originalFilename;
	private Long offset;


	public String getMd5() {
		return md5;
	}

	public <T extends FileResourceCreate> T setMd5(String md5) {
		this.md5 = md5;
		return (T) this;
	}

	public String getFullPath() {
		return fullPath;
	}

	public <T extends FileResourceCreate> T setFullPath(String fullPath) {
		this.fullPath = fullPath;
		return (T) this;
	}

	public String getActualFilename() {
		return actualFilename;
	}

	public <T extends FileResourceCreate> T setActualFilename(String actualFilename) {
		this.actualFilename = actualFilename;
		return (T) this;
	}

	public String getOriginalFilename() {
		return originalFilename;
	}

	public <T extends FileResourceCreate> T setOriginalFilename(String originalFilename) {
		this.originalFilename = originalFilename;
		return (T) this;
	}

	public Long getOffset() {
		return offset;
	}

	public <T extends FileResourceCreate> T setOffset(Long offset) {
		this.offset = offset;
		return (T) this;
	}
}
