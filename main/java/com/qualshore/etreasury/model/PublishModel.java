package com.qualshore.etreasury.model;

import java.util.List;

public class PublishModel {

	Integer idDoc;
	List<Integer> idsInstitution;
	public List<Integer> getIdsInstitution() {
		return idsInstitution;
	}
	public void setIdsInstitution(List<Integer> idsInstitution) {
		this.idsInstitution = idsInstitution;
	}
	List<Integer> idUsers;
	
	public PublishModel() {
		super();
	}
	public Integer getIdDoc() {
		return idDoc;
	}
	public void setIdDoc(Integer idDoc) {
		this.idDoc = idDoc;
	}
	
	
}