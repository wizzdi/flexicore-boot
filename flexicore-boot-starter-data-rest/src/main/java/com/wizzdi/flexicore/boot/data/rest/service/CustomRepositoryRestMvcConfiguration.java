package com.wizzdi.flexicore.boot.data.rest.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.geo.GeoModule;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.hateoas.mediatype.MessageResolver;
import org.springframework.hateoas.mediatype.hal.CurieProvider;
import org.springframework.hateoas.mediatype.hal.HalConfiguration;
import org.springframework.hateoas.server.LinkRelationProvider;
import org.springframework.hateoas.server.mvc.RepresentationModelProcessorInvoker;
import org.springframework.web.util.pattern.PathPatternParser;

@Configuration
public class CustomRepositoryRestMvcConfiguration extends RepositoryRestMvcConfiguration {

	public CustomRepositoryRestMvcConfiguration(ApplicationContext context, ObjectFactory<ConversionService> conversionService, ObjectProvider<LinkRelationProvider> relProvider, ObjectProvider<CurieProvider> curieProvider, ObjectProvider<HalConfiguration> halConfiguration, ObjectProvider<ObjectMapper> objectMapper, ObjectProvider<RepresentationModelProcessorInvoker> invoker, ObjectProvider<MessageResolver> resolver, ObjectProvider<GeoModule> geoModule, ObjectProvider<PathPatternParser> parser) {
		super(context, conversionService, relProvider, curieProvider, halConfiguration, objectMapper, invoker, resolver, geoModule, parser);
	}


}
