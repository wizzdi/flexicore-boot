package com.wizzdi.flexicore.boot.websockets.pluginA.encoders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import org.pf4j.Extension;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import com.wizzdi.flexicore.boot.websockets.pluginA.messages.WSEvent;

import jakarta.websocket.DecodeException;
import jakarta.websocket.Decoder;
import jakarta.websocket.EndpointConfig;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Reader;

/**
 * Created by Asaf on 12/02/2017.
 */
@Extension
@Component
public class WSDecoder implements Decoder.TextStream<WSEvent>, Plugin, ApplicationContextAware {

	private static ObjectMapper objectMapper;

	@Override
	public void init(EndpointConfig config) {

	}

	@Override
	public void destroy() {

	}

	@Override
	public WSEvent decode(Reader reader) throws DecodeException, IOException {
		return objectMapper.readValue(reader, WSEvent.class);

	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		objectMapper=applicationContext.getBean(ObjectMapper.class);
	}
}
