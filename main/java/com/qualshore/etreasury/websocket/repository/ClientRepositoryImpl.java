package com.qualshore.etreasury.websocket.repository;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.qualshore.etreasury.websocket.Client;

@Repository
@Scope("singleton")
public class ClientRepositoryImpl extends ClientRepository {

private List<Client> clients = new LinkedList<>();
	
	@Override
	public void add(Client client) {
		synchronized (this.clients) {
			this.clients.add(client);
		}
	}
	
	@Override
	public void remove(Client client) {
		synchronized (this.clients) {
			this.clients.remove(client);
		}
	}
	
	@Override
	public void forEach(Consumer<Client> clientConsume) {
		synchronized (this.clients) {
			this.clients.forEach(clientConsume);
		}
	}

	@Override
	public List<Client> getAll() {
		return new LinkedList<>(this.clients);
	}
}
