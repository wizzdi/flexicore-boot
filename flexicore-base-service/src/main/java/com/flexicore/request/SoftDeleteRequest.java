package com.flexicore.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.Basic;

public class SoftDeleteRequest  {

	private String id;
	private String type;
	@JsonIgnore
	private Class<? extends Basic> clazz;
	@JsonIgnore
	private Basic basic;

	public String getId() {
		return id;
	}

	public <T extends SoftDeleteRequest> T setId(String id) {
		this.id = id;
		return (T) this;
	}

	public String getType() {
		return type;
	}

	public <T extends SoftDeleteRequest> T setType(String type) {
		this.type = type;
		return (T) this;
	}

	public Class<? extends Basic> getClazz() {
		return clazz;
	}

	public <T extends SoftDeleteRequest> T setClazz(Class<? extends Basic> clazz) {
		this.clazz = clazz;
		return (T) this;
	}

	public Basic getBasic() {
		return basic;
	}

	public <T extends SoftDeleteRequest> T setBasic(Basic basic) {
		this.basic = basic;
		return (T) this;
	}
}
