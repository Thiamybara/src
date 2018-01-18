package com.qualshore.etreasury.model;

import java.util.List;

public class JuridictionGroupesModel {

	private Integer idGroupes;
	public Integer getIdGroupes() {
		return idGroupes;
	}
	public void setIdGroupes(Integer idGroupes) {
		this.idGroupes = idGroupes;
	}
	public List<Integer> getIdJuridiction() {
		return idJuridiction;
	}
	public void setIdJuridiction(List<Integer> idJuridiction) {
		this.idJuridiction = idJuridiction;
	}
	private List<Integer> idJuridiction;
	/*
	public Integer getIdJuridiction() {
		return idJuridiction;
	}
	public void setIdJuridiction(Integer idJuridiction) {
		this.idJuridiction = idJuridiction;
	}
	public List<Integer> getIdGroupes() {
		return idGroupes;
	}
	public void setIdGroupes(List<Integer> idGroupes) {
		this.idGroupes = idGroupes;
	}
	*/
}
