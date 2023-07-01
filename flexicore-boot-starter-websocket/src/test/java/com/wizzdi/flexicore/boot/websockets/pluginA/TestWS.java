package com.wizzdi.flexicore.boot.websockets.pluginA;


import com.wizzdi.flexicore.boot.base.annotations.plugins.PluginInfo;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import org.pf4j.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import com.wizzdi.flexicore.boot.websockets.pluginA.encoders.WSDecoder;
import com.wizzdi.flexicore.boot.websockets.pluginA.encoders.WSEncoder;
import com.wizzdi.flexicore.boot.websockets.pluginA.messages.TestMessage;
import com.wizzdi.flexicore.boot.websockets.pluginA.messages.WSEvent;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

/**
 * Created by Asaf on 31/08/2016.
 */
@ServerEndpoint(value = "/wsTest/{authenticationKey}", encoders = {WSEncoder.class}, decoders = {WSDecoder.class})
@PluginInfo(version = 1)
@Extension
@Component
public class TestWS implements Plugin {

	private static final Logger logger=LoggerFactory.getLogger(TestWS.class);

	@Autowired
	private ApplicationEventPublisher eventReceivedContainerEvent;

	@OnMessage
	public void onMessage(WSEvent message, Session session) {
		logger.info("received message " + message);
		eventReceivedContainerEvent.publishEvent(new EventReceivedContainer()
				.setWsEvent(message).setSession(session));
	}

	@OnOpen
	public void open(@PathParam("authenticationKey") String authenticationKey,
			Session session) {
		logger.info("Opening:" + session.getId());
		try {
			session.getBasicRemote().sendObject(new TestMessage().setTest("test: " + session.getId()));
		}
		catch (Exception e){
			logger.error("failed sending hello message",e);
		}
		UiEventSender.registerUISession(session);
	}

	@OnClose
	public void close(@PathParam("authenticationKey") String authenticationKey,
			CloseReason c, Session session) {
		logger.info("Closing:" + session.getId());
		UiEventSender.unregisterSession(session);
	}

}
