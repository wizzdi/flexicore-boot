package com.wizzdi.flexicore.security.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.Clazz;

public class ClazzUpdate extends ClazzCreate{

	private String id;
	@JsonIgnore
	private Clazz Clazz;

	public String getId() {
		return id;
	}

	public <T extends ClazzUpdate> T setId(String id) {
		this.id = id;
		return (T) this;
	}

	@JsonIgnore
	public Clazz getClazz() {
		return Clazz;
	}

	public <T extends ClazzUpdate> T setClazz(Clazz Clazz) {
		this.Clazz = Clazz;
		return (T) this;
	}
}
