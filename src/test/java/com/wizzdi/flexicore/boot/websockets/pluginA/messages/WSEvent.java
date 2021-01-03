package com.wizzdi.flexicore.boot.websockets.pluginA.messages;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import com.wizzdi.flexicore.boot.rest.resolvers.CrossLoaderResolver;

@JsonTypeInfo(
		use = JsonTypeInfo.Id.CLASS,
		property = "type"
)
@JsonTypeIdResolver(CrossLoaderResolver.class)
public interface WSEvent {
}
