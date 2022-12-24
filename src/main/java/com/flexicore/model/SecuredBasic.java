package com.flexicore.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class SecuredBasic extends Basic {

	@JsonIgnore
	@ManyToOne(targetEntity = Baseclass.class,cascade = CascadeType.MERGE)
	private Baseclass security;

	@JsonIgnore
	@ManyToOne(targetEntity = Baseclass.class,cascade = CascadeType.MERGE)
	public Baseclass getSecurity() {
		return security;
	}

	public <T extends SecuredBasic> T setSecurity(Baseclass security) {
		this.security = security;
		return (T) this;
	}
}
