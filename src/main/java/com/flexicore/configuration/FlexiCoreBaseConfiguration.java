package com.flexicore.configuration;


import com.flexicore.interfaces.ServicePlugin;
import org.pf4j.Extension;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@Extension
@Configuration
@EnableWebSocket
@EnableMongoRepositories
public class FlexiCoreBaseConfiguration implements ServicePlugin {
}
