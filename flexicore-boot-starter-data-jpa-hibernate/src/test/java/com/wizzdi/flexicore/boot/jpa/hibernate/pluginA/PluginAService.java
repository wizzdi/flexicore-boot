package com.wizzdi.flexicore.boot.jpa.hibernate.pluginA;

import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.boot.jpa.hibernate.app.TestEntity;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Extension
@Component
public class PluginAService implements Plugin {

	@Autowired
	private PluginARepository pluginARepository;

	public TestEntity createTestEntity(TestEntityCreate testEntityCreate){
		TestEntity testEntity=new TestEntity()
				.setLongText(testEntityCreate.getLongText())
				.setName(testEntityCreate.getName())
				.setId(UUID.randomUUID().toString())
				.setInheritedString(testEntityCreate.getInheritedString());

		pluginARepository.merge(testEntity);
		return testEntity;
	}

	public TestEntity getTestEntity(String id){
		return pluginARepository.find(id);
	}
}
