package com.qualshore.etreasury.websocket.repository;

import javax.websocket.CloseReason;
import javax.websocket.Session;

public interface WebSocketHandler {

	public void onOpen(Session userSession);
	public void onClose(CloseReason closeReason, Session userSession);
}
