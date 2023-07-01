package com.wizzdi.flexicore.boot.jpa.hibernate.pluginA;

import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.boot.jpa.hibernate.app.BaseEntity;
import com.wizzdi.flexicore.boot.jpa.hibernate.app.TestEntity;
import com.wizzdi.flexicore.boot.jpa.init.hibernate.EncryptionConfiguration;
import com.wizzdi.flexicore.boot.jpa.init.hibernate.EncryptionConfigurations;
import com.wizzdi.flexicore.boot.jpa.init.hibernate.StandardEncryptionConfiguration;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Arrays;
import java.util.Collections;

@Extension
@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
public class Config implements Plugin {


}
