package com.wizzdi.flexicore.security.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.Clazz;
import com.flexicore.model.SecurityTenant;
import com.flexicore.model.SecurityUser;

import java.time.OffsetDateTime;

public class BaseclassCreate {

	private String name;
	private String description;
	@JsonIgnore
	private OffsetDateTime updateDate;
	@JsonIgnore
	private SecurityTenant tenant;
	private String tenantId;
	private Boolean softDelete;
	private String idForCreate;
	private Boolean systemObject;


	@JsonIgnore
	public OffsetDateTime getUpdateDate() {
		return updateDate;
	}

	public <T extends BaseclassCreate> T setUpdateDate(OffsetDateTime updateDate) {
		this.updateDate = updateDate;
		return (T) this;
	}

	public String getName() {
		return name;
	}

	public <T extends BaseclassCreate> T setName(String name) {
		this.name = name;
		return (T) this;
	}

	public String getDescription() {
		return description;
	}

	public <T extends BaseclassCreate> T setDescription(String description) {
		this.description = description;
		return (T) this;
	}

	@JsonIgnore
	public SecurityTenant getTenant() {
		return tenant;
	}

	public <T extends BaseclassCreate> T setTenant(SecurityTenant tenant) {
		this.tenant = tenant;
		return (T) this;
	}

	public String getTenantId() {
		return tenantId;
	}

	public <T extends BaseclassCreate> T setTenantId(String tenantId) {
		this.tenantId = tenantId;
		return (T) this;
	}

	public Boolean getSoftDelete() {
		return softDelete;
	}

	public <T extends BaseclassCreate> T setSoftDelete(Boolean softDelete) {
		this.softDelete = softDelete;
		return (T) this;
	}

	public String getIdForCreate() {
		return idForCreate;
	}

	public <T extends BaseclassCreate> T setIdForCreate(String idForCreate) {
		this.idForCreate = idForCreate;
		return (T) this;
	}

	public Boolean getSystemObject() {
		return systemObject;
	}

	public <T extends BaseclassCreate> T setSystemObject(Boolean systemObject) {
		this.systemObject = systemObject;
		return (T) this;
	}
}
