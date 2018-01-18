package com.qualshore.etreasury.websocket;

public class Task {

	private String id;
	private Client owner;
	private String title;
	private String description;
	private long duration;
	private boolean universal;
	
	public Task() {
		this.id = this.toString();
	}
	
	public Client getOwner() {
		return owner;
	}
	
	public void setOwner(Client owner) {
		this.owner = owner;
	}
	
	public boolean isUniversal() {
		return universal;
	}
	
	public void setUniversal(boolean universal) {
		this.universal = universal;
	}
	
	public String getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}
}
