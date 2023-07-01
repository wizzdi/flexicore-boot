package com.wizzdi.flexicore.boot.websockets.pluginA.encoders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import com.wizzdi.flexicore.boot.websockets.pluginA.messages.WSEvent;
import org.pf4j.Extension;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import jakarta.websocket.Encoder;
import jakarta.websocket.EndpointConfig;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Writer;

/**
 * Created by Asaf on 12/02/2017.
 */
@Extension
@Component
public class WSEncoder implements Encoder.TextStream<WSEvent>, Plugin, ApplicationContextAware {

	private static ObjectMapper objectMapper;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		objectMapper=applicationContext.getBean(ObjectMapper.class);
	}

	@Override
	public void init(EndpointConfig config) {


	}

	@Override
	public void destroy() {

	}

	@Override
	public void encode(WSEvent object, Writer writer) throws IOException {
		objectMapper.writeValue(writer, object);

	}
}
