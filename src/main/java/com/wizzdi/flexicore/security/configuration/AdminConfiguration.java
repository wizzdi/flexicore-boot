package com.wizzdi.flexicore.security.configuration;

import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@Extension
public class AdminConfiguration implements Plugin {
	@Bean
	@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
	@ConditionalOnMissingBean
	@Qualifier("systemAdminId")
	public String systemAdminId(){
		return "UEKbB6XlQhKOtjziJoUQ8w";
	}

}
