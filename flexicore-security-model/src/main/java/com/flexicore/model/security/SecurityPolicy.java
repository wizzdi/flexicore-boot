package com.flexicore.model.security;

import com.flexicore.model.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import java.time.OffsetDateTime;

@Entity
public class SecurityPolicy extends Baseclass {


	@Column(columnDefinition = "timestamp with time zone")
	private OffsetDateTime startTime;
	private boolean enabled;
	@ManyToOne(targetEntity = Role.class)
	private Role policyRole;
	@ManyToOne(targetEntity = SecurityTenant.class)
	private SecurityTenant policyTenant;



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


}
