package com.wizzdi.flexicore.boot.dynamic.invokers.service.plugin;

public class TestEntity {

	private String name;
	private String description;

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public <T extends TestEntity> T setName(String name) {
		this.name = name;
		return (T) this;
	}

	public <T extends TestEntity> T setDescription(String description) {
		this.description = description;
		return (T) this;
	}
}
