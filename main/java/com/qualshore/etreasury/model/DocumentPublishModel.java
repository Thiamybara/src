package com.qualshore.etreasury.model;

import java.io.Serializable;
import java.util.List;

public class DocumentPublishModel implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Integer idDocument;
	
	private List<Integer> objectList;
	
	public DocumentPublishModel() {
	}

	public Integer getIdDocument() {
		return idDocument;
	}

	public void setIdDocument(Integer idDocument) {
		this.idDocument = idDocument;
	}

	public List<Integer> getObjectList() {
		return objectList;
	}

	public void setObjectList(List<Integer> objectList) {
		this.objectList = objectList;
	}
}