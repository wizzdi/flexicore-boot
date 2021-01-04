package com.flexicore.annotations.plugins;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@com.wizzdi.flexicore.boot.base.annotations.plugins.PluginInfo(version = 1)
public @interface PluginInfo {


	@AliasFor(annotation = com.wizzdi.flexicore.boot.base.annotations.plugins.PluginInfo.class, attribute = "version")
	int version();


}
