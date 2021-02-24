package com.flexicore.model.security;

import com.flexicore.model.Baseclass;
import com.flexicore.model.Basic;
import com.flexicore.model.Role;
import com.flexicore.model.SecurityTenant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.time.OffsetDateTime;

@Entity
public class SecurityPolicy extends Basic {


	@Column(columnDefinition = "timestamp with time zone")
	private OffsetDateTime startTime;
	private boolean enabled;
	@ManyToOne(targetEntity = Role.class)
	private Role policyRole;
	@ManyToOne(targetEntity = SecurityTenant.class)
	private SecurityTenant policyTenant;
	@ManyToOne(targetEntity = Baseclass.class)
	private Baseclass security;


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

}
