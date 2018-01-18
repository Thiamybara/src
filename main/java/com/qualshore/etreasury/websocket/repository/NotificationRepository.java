package com.qualshore.etreasury.websocket.repository;

import java.util.Observer;

import javax.annotation.PostConstruct;
import javax.management.Notification;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.qualshore.etreasury.entity.Notifications;
import com.qualshore.etreasury.websocket.Task;

@Repository
public abstract class NotificationRepository extends NotificationAppRepository<String, Notifications> {

	@Autowired
	private BeanFactory factory;
	
	@PostConstruct
	public void init() {
		this.addObserver((Observer) this.factory.getBean(NotificationRepositoryObserver.class));
	}
}
