package com.wizzdi.flexicore.boot.websockets.pluginA;


import com.wizzdi.flexicore.boot.base.annotations.plugins.PluginInfo;
import com.wizzdi.flexicore.boot.base.interfaces.Plugin;
import org.pf4j.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import com.wizzdi.flexicore.boot.websockets.pluginA.messages.WSEvent;

import javax.annotation.Priority;
import jakarta.websocket.EncodeException;
import jakarta.websocket.Session;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

@PluginInfo(version = 1)
@Extension
@Component
public class UiEventSender implements Plugin {

	private static Queue<Session> sessions = new LinkedBlockingQueue<>();

	private static final Logger logger = LoggerFactory.getLogger(UiEventSender.class);
	private long expireAfterAccessSeconds = 60 * 60;


	@EventListener
	public void sendEvent(@Priority(0) WSEvent wsEvent) {
		List<Session> toRemove = new ArrayList<>();
		logger.info("Received event " + wsEvent + " to send to "
				+ sessions.size() + " sessions");
		for (Session session : sessions) {
			try {
				if (!session.isOpen()) {
					toRemove.add(session);
					continue;
				}
				session.getBasicRemote().sendObject(wsEvent);

			} catch (EncodeException | IOException e) {
				logger.error("unable to send message", e);
				try {
					session.close();
				} catch (IOException e1) {
					logger.error("unable to close session");
				}
				toRemove.add(session);
			}
		}
		sessions.removeAll(toRemove);
	}


	public static void registerUISession(Session session) {
		sessions.add(session);
	}

	public static void unregisterSession(Session session) {
		sessions.remove(session);
	}

}
