package com.qualshore.etreasury.websocket;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.qualshore.etreasury.entity.Notifications;
import com.qualshore.etreasury.entity.NotificationsPK;
import com.qualshore.etreasury.websocket.repository.NotificationRepository;

@RestController
public class NotificationRestController {
	@Autowired
	private NotificationRepository taskRepository;
	
	@RequestMapping(path = "/notification", method = RequestMethod.GET)
	public @ResponseBody List<Notifications> getTasks() {
		Notifications notification = new Notifications(new NotificationsPK(6, 7, 9), new Date());
		this.taskRepository.add(notification);
		
		return this.taskRepository.getAll();
	}
	
	@RequestMapping(path = "/tasks", method = RequestMethod.POST)
	public void addTask(@RequestBody Notifications task) {
		this.taskRepository.add(task);
	}
}
