package com.wizzdi.flexicore.security.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.SecurityTenant;

import java.time.OffsetDateTime;

public class BasicCreate {

	private String name;
	private String description;
	@JsonIgnore
	private OffsetDateTime updateDate;
	private Boolean softDelete;
	private String idForCreate;


	@JsonIgnore
	public OffsetDateTime getUpdateDate() {
		return updateDate;
	}

	public <T extends BasicCreate> T setUpdateDate(OffsetDateTime updateDate) {
		this.updateDate = updateDate;
		return (T) this;
	}

	public String getName() {
		return name;
	}

	public <T extends BasicCreate> T setName(String name) {
		this.name = name;
		return (T) this;
	}

	public String getDescription() {
		return description;
	}

	public <T extends BasicCreate> T setDescription(String description) {
		this.description = description;
		return (T) this;
	}


	public Boolean getSoftDelete() {
		return softDelete;
	}

	public <T extends BasicCreate> T setSoftDelete(Boolean softDelete) {
		this.softDelete = softDelete;
		return (T) this;
	}

	public String getIdForCreate() {
		return idForCreate;
	}

	public <T extends BasicCreate> T setIdForCreate(String idForCreate) {
		this.idForCreate = idForCreate;
		return (T) this;
	}

}
