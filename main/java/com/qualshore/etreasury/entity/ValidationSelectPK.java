package com.qualshore.etreasury.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.validation.constraints.NotNull;

public class ValidationSelectPK implements Serializable {

	private static final long serialVersionUID = 1L;

	@Basic(optional = false)
    @NotNull
	@Column(name="offer")
	private Integer offer;
	
	@Column(name = "niveau")
	private Integer niveau;

	public ValidationSelectPK() {
	}
	
	public ValidationSelectPK(Integer offer, Integer niveau) {
		this.offer = offer;
		this.niveau = niveau;
	}

	public Integer getOffer() {
		return offer;
	}

	public void setOffer(Integer offer) {
		this.offer = offer;
	}

	public Integer getNiveau() {
		return niveau;
	}

	public void setNiveau(Integer niveau) {
		this.niveau = niveau;
	}
}
