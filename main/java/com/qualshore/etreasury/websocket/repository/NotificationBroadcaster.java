package com.qualshore.etreasury.websocket.repository;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.management.Notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.qualshore.etreasury.entity.Notifications;
import com.qualshore.etreasury.websocket.Task;

@Component
public class NotificationBroadcaster implements Broadcaster<Notifications> {

	@Autowired
	private ClientRepository clients;
	private Gson gson;
	
	@PostConstruct
	public void init() {
		this.gson = new Gson();
	}
	
	@Override
	public void broadcast(List<Notifications> task) {
		this.clients.forEach(client -> {
			try {
				client.sendText(this.gson.toJson(task));
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
}
