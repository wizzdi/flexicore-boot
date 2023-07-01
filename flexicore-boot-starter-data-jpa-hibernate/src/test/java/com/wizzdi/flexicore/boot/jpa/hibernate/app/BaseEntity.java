package com.wizzdi.flexicore.boot.jpa.hibernate.app;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public class BaseEntity {

	@Id
	private String id;
	private String inheritedString;

	public String getInheritedString() {
		return inheritedString;
	}

	public <T extends BaseEntity> T setInheritedString(String inheritedString) {
		this.inheritedString = inheritedString;
		return (T) this;
	}

	@Id
	public String getId() {
		return id;
	}

	public <T extends BaseEntity> T setId(String id) {
		this.id = id;
		return (T) this;
	}
}
