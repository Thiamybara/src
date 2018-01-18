package com.qualshore.etreasury.websocket.repository;

import java.util.Observable;
import java.util.Observer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NotificationRepositoryObserver implements Observer {

	@Autowired
	private NotificationBroadcaster broadcaster;
	
	@Override
	public void update(Observable repository, Object param) {
		NotificationRepository repo = (NotificationRepository) repository;
		this.broadcaster.broadcast(repo.getAll());
	}
}
