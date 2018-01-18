package com.qualshore.etreasury.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class ValidationRequestPK implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Basic(optional = false)
    @NotNull
	@Column(name="request")
	private Integer request;
	
	/*@Basic(optional = false)
    @NotNull
	@Column(name="user")
	private Integer user;*/
	
	@Column(name = "niveau")
	private Integer niveau;

	public ValidationRequestPK() {
	}
	
	public ValidationRequestPK(Integer request, Integer niveau) {
		this.request = request;
		this.niveau = niveau;
	}

	/*public Integer getUser() {
		return user;
	}

	public void setUser(Integer user) {
		this.user = user;
	}*/

	public Integer getRequest() {
		return request;
	}

	public void setRequest(Integer request) {
		this.request = request;
	}

	public Integer getNiveau() {
		return niveau;
	}

	public void setNiveau(Integer niveau) {
		this.niveau = niveau;
	}
}
