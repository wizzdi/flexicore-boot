package com.wizzdi.flexicore.security.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.Clazz;
import com.flexicore.model.Tenant;
import com.flexicore.model.SecurityUser;

import java.time.OffsetDateTime;

public class BaseclassCreate {

	private String name;
	private String description;
	@JsonIgnore
	private OffsetDateTime updateDate;
	@JsonIgnore
	private Tenant tenant;
	private String tenantId;


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
	public Tenant getTenant() {
		return tenant;
	}

	public <T extends BaseclassCreate> T setTenant(Tenant tenant) {
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
}
