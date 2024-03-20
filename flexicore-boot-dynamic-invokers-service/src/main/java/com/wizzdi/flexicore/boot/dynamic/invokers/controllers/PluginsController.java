package com.wizzdi.flexicore.boot.dynamic.invokers.controllers;

import com.flexicore.annotations.IOperation;
import com.flexicore.annotations.OperationsInside;
import com.flexicore.security.SecurityContextBase;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.boot.dynamic.invokers.response.PluginInfo;
import org.pf4j.Extension;
import org.pf4j.PluginManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@OperationsInside
@RequestMapping("/plugins")
@Extension
public class PluginsController implements Plugin {

	@Autowired
	@Lazy
	private PluginManager pluginManager;



	@IOperation(Name = "returns plugins",Description = "returns plugins")
	@GetMapping
	public List<PluginInfo> getAll(@RequestAttribute SecurityContextBase securityContext){
		return pluginManager.getStartedPlugins().stream().map(f->new PluginInfo(f.getPluginId(),f.getDescriptor().getVersion())).collect(Collectors.toList());
	}





}
