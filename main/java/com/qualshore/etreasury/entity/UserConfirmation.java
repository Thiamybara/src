package com.qualshore.etreasury.entity;

import java.io.Serializable;

public class UserConfirmation implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer idUser;
	private String token;
	
	public UserConfirmation() {}

	public Integer getIdUser() {
		return idUser;
	}

	public void setIdUser(Integer idUser) {
		this.idUser = idUser;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
