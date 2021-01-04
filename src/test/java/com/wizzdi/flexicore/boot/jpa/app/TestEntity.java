package com.wizzdi.flexicore.boot.jpa.app;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class TestEntity {

	@Id
	private String id;

	private String name;

	@Id
	public String getId() {
		return id;
	}

	public <T extends TestEntity> T setId(String id) {
		this.id = id;
		return (T) this;
	}

	public String getName() {
		return name;
	}

	public <T extends TestEntity> T setName(String name) {
		this.name = name;
		return (T) this;
	}
}
