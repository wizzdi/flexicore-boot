package com.wizzdi.flexicore.security.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.Baseclass;
import com.flexicore.model.Role;
import com.flexicore.model.SecurityTenant;

import java.time.OffsetDateTime;

public class SecurityPolicyCreate  {


	private String name;
	private String description;
	private OffsetDateTime startTime;
	private Boolean enabled;
	private String policyRoleId;
	@JsonIgnore
	private Role policyRole;
	private String policyTenantId;
	@JsonIgnore
	private SecurityTenant policyTenant;
	private String securityId;
	@JsonIgnore
	private Baseclass security;


	public OffsetDateTime getStartTime() {
		return startTime;
	}

	public <T extends SecurityPolicyCreate> T setStartTime(OffsetDateTime startTime) {
		this.startTime = startTime;
		return (T) this;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public <T extends SecurityPolicyCreate> T setEnabled(Boolean enabled) {
		this.enabled = enabled;
		return (T) this;
	}

	public String getPolicyRoleId() {
		return policyRoleId;
	}

	public <T extends SecurityPolicyCreate> T setPolicyRoleId(String policyRoleId) {
		this.policyRoleId = policyRoleId;
		return (T) this;
	}

	@JsonIgnore
	public Role getPolicyRole() {
		return policyRole;
	}

	public <T extends SecurityPolicyCreate> T setPolicyRole(Role policyRole) {
		this.policyRole = policyRole;
		return (T) this;
	}

	public String getPolicyTenantId() {
		return policyTenantId;
	}

	public <T extends SecurityPolicyCreate> T setPolicyTenantId(String policyTenantId) {
		this.policyTenantId = policyTenantId;
		return (T) this;
	}

	@JsonIgnore
	public SecurityTenant getPolicyTenant() {
		return policyTenant;
	}

	public <T extends SecurityPolicyCreate> T setPolicyTenant(SecurityTenant policyTenant) {
		this.policyTenant = policyTenant;
		return (T) this;
	}

	public String getSecurityId() {
		return securityId;
	}

	public <T extends SecurityPolicyCreate> T setSecurityId(String securityId) {
		this.securityId = securityId;
		return (T) this;
	}

	@JsonIgnore
	public Baseclass getSecurity() {
		return security;
	}

	public <T extends SecurityPolicyCreate> T setSecurity(Baseclass security) {
		this.security = security;
		return (T) this;
	}

	public String getName() {
		return name;
	}

	public <T extends SecurityPolicyCreate> T setName(String name) {
		this.name = name;
		return (T) this;
	}

	public String getDescription() {
		return description;
	}

	public <T extends SecurityPolicyCreate> T setDescription(String description) {
		this.description = description;
		return (T) this;
	}
}
