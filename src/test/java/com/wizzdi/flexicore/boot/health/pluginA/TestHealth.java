package com.wizzdi.flexicore.boot.health.pluginA;

import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import org.pf4j.Extension;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
@Extension
public class TestHealth implements HealthIndicator, Plugin {



	@Override
	public Health health() {
		return new Health.Builder().withDetail("test","123").up().build();
	}
}
