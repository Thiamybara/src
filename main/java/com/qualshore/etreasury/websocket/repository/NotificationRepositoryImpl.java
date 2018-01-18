package com.qualshore.etreasury.websocket.repository;

import java.util.LinkedList;
import java.util.List;
import java.util.Observer;
import java.util.function.Consumer;

import javax.annotation.PostConstruct;
import javax.management.Notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.qualshore.etreasury.entity.Notifications;
import com.qualshore.etreasury.websocket.Task;

@Repository
@Scope("singleton")
public class NotificationRepositoryImpl extends NotificationRepository {

	@Autowired
	private NotificationRepositoryObserver observer;
	
	private List<Notifications> tasks = new LinkedList<>();
	
	@PostConstruct
	public void init() {
		this.addObserver((Observer) observer);
	}
	
	@Override
	public void add(Notifications task) {
		synchronized (tasks) {
			this.tasks.add(task);
		}
		
		this.publish();
	}

	@Override
	public void remove(Notifications task) {
		synchronized (tasks) {
			this.tasks.remove(task);
		}
		
		this.publish();
	}

	@Override
	public void forEach(Consumer<Notifications> typeConsumer) {
		synchronized (tasks) {
			this.tasks.forEach(typeConsumer);
		}
	}
	
	public List<Notifications> getAll() {
		return new LinkedList<>(this.tasks);
	}
}
