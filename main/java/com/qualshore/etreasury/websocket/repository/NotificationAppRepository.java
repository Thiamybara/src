package com.qualshore.etreasury.websocket.repository;

import java.util.List;
import java.util.Observable;
import java.util.function.Consumer;

public abstract class NotificationAppRepository<K, T> extends Observable {

	public abstract void add(T type);
	public abstract void remove(T type);
	public abstract void forEach(Consumer<T> typeConsumer);
	public abstract List<T> getAll();

	protected void publish() {
		this.setChanged();
		this.notifyObservers();
	}
}
