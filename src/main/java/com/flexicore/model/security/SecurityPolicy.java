package com.flexicore.model.security;

import com.flexicore.model.Baseclass;
import com.flexicore.model.Role;
import com.flexicore.model.SecurityTenant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.OffsetDateTime;

@Entity
public class SecurityPolicy {

	@Id
	private String id;
	private String name;
	private String description;

	@Column(columnDefinition = "timestamp with time zone")
	private OffsetDateTime startTime;
	private boolean enabled;
	@ManyToOne(targetEntity = Role.class)
	private Role policyRole;
	@ManyToOne(targetEntity = SecurityTenant.class)
	private SecurityTenant policyTenant;
	@ManyToOne(targetEntity = Baseclass.class)
	private Baseclass security;

	@Id
	public String getId() {
		return id;
	}

	public <T extends SecurityPolicy> T setId(String id) {
		this.id = id;
		return (T) this;
	}

	@Column(columnDefinition = "timestamp with time zone")
	public OffsetDateTime getStartTime() {
		return startTime;
	}

	public <T extends SecurityPolicy> T setStartTime(OffsetDateTime startTime) {
		this.startTime = startTime;
		return (T) this;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public <T extends SecurityPolicy> T setEnabled(boolean enabled) {
		this.enabled = enabled;
		return (T) this;
	}

	@ManyToOne(targetEntity = Role.class)
	public Role getPolicyRole() {
		return policyRole;
	}

	public <T extends SecurityPolicy> T setPolicyRole(Role policyRole) {
		this.policyRole = policyRole;
		return (T) this;
	}

	@ManyToOne(targetEntity = SecurityTenant.class)
	public SecurityTenant getPolicyTenant() {
		return policyTenant;
	}

	public <T extends SecurityPolicy> T setPolicyTenant(SecurityTenant policyTenant) {
		this.policyTenant = policyTenant;
		return (T) this;
	}

	@ManyToOne(targetEntity = Baseclass.class)
	public Baseclass getSecurity() {
		return security;
	}

	public <T extends SecurityPolicy> T setSecurity(Baseclass security) {
		this.security = security;
		return (T) this;
	}

	public String getName() {
		return name;
	}

	public <T extends SecurityPolicy> T setName(String name) {
		this.name = name;
		return (T) this;
	}

	public String getDescription() {
		return description;
	}

	public <T extends SecurityPolicy> T setDescription(String description) {
		this.description = description;
		return (T) this;
	}
}
