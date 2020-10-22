package com.flexicore.configuration;


import com.flexicore.interfaces.ServicePlugin;
import org.pf4j.Extension;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@Extension
@Configuration
@ComponentScan(basePackages = "com.flexicore")
@EntityScan(basePackages = {"com.flexicore.model"})
@EnableWebSocket
@EnableMongoRepositories
@EnableJpaRepositories(basePackages = {"com.flexicore.data"})
public class FlexiCoreBaseConfiguration implements ServicePlugin {
}
