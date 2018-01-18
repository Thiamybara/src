package com.qualshore.etreasury.model;

import java.util.Date;
import java.util.List;

import com.qualshore.etreasury.entity.Products;

public class BankConditionRechModel {
	
	String dateDebut;
	String dateFin;
	String tauxStandard;
	Integer idPr;
	List<Integer> idsBank;
	
	public String getDateDebut() {
		return dateDebut;
	}
	public void setDateDebut(String dateDebut) {
		this.dateDebut = dateDebut;
	}
	public String getDateFin() {
		return dateFin;
	}
	public void setDateFin(String dateFin) {
		this.dateFin = dateFin;
	}
	public String getTauxStandard() {
		return tauxStandard;
	}
	public void setTauxStandard(String tauxStandard) {
		this.tauxStandard = tauxStandard;
	}
	public Integer getIdPr() {
		return idPr;
	}
	public void setIdPr(Integer idPr) {
		this.idPr = idPr;
	}
	public List<Integer> getIdsBank() {
		return idsBank;
	}
	public void setIdsBank(List<Integer> idsBank) {
		this.idsBank = idsBank;
	}
}
	
