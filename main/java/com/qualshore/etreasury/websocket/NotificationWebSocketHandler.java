package com.qualshore.etreasury.websocket;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.server.standard.SpringConfigurator;

import com.qualshore.etreasury.websocket.repository.ClientRepository;
import com.qualshore.etreasury.websocket.repository.WebSocketHandler;

@ServerEndpoint(value = "/notification", configurator = SpringConfigurator.class)
public class NotificationWebSocketHandler implements WebSocketHandler {

	@Autowired
	private ClientRepository clientRepository;
  
	@OnOpen
	public void onOpen(Session session) {
		//this.clientRepository.add(new Client(session));
		try {
			this.clientRepository.add(new Client());
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
  
	@OnClose
	public void onClose(CloseReason reason, Session session) {
		//this.clientRepository.remove(new Client(session));
		try {
			this.clientRepository.remove(new Client());
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}	
}
