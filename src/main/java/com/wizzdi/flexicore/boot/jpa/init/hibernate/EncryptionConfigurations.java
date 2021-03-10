package com.wizzdi.flexicore.boot.jpa.init.hibernate;

import java.util.List;

public class EncryptionConfigurations {

	private final List<EncryptionConfiguration> encryptionConfigurations;

	public EncryptionConfigurations(List<EncryptionConfiguration> encryptionConfigurations) {
		this.encryptionConfigurations = encryptionConfigurations;
	}


	public List<EncryptionConfiguration> getEncryptionConfigurations() {
		return encryptionConfigurations;
	}
}
