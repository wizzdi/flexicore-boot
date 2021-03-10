package com.wizzdi.flexicore.boot.jpa.hibernate.app;

import com.wizzdi.flexicore.boot.jpa.init.hibernate.EncryptionConfigurations;
import com.wizzdi.flexicore.boot.jpa.init.hibernate.StandardEncryptionConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class EncryptionConfig {


	@Value("${flexicore.database.encryptionKey.test:this is a test}")
	private String key;

	@Bean
	public EncryptionConfigurations encryptionConfigurations() throws NoSuchMethodException {
		return new EncryptionConfigurations(Arrays.asList(
				new StandardEncryptionConfiguration(key, "name", TestEntity.class.getDeclaredMethod("getName")),
				new StandardEncryptionConfiguration(key,true, "long_text", TestEntity.class.getDeclaredMethod("getLongText")),
				new StandardEncryptionConfiguration(key,false, "inherited_string", TestEntity.class.getMethod("getInheritedString"))


		));
	}
}
