package plugins;


import plugins.messages.WSEvent;

import jakarta.websocket.Session;

public class EventReceivedContainer {

	private WSEvent wsEvent;
	private Session session;

	public WSEvent getWsEvent() {
		return wsEvent;
	}

	public <T extends EventReceivedContainer> T setWsEvent(WSEvent wsEvent) {
		this.wsEvent = wsEvent;
		return (T) this;
	}

	public Session getSession() {
		return session;
	}

	public <T extends EventReceivedContainer> T setSession(Session session) {
		this.session = session;
		return (T) this;
	}
}
