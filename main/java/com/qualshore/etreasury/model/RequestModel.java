package com.qualshore.etreasury.model;

import java.io.Serializable;

import com.qualshore.etreasury.entity.Request;

public class RequestModel implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Request request;
	
	private Integer[] documents;
	
	private Integer[] idsBank;

	public RequestModel() {
	}

	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;
	}

	public Integer[] getDocuments() {
		return documents;
	}

	public void setDocuments(Integer[] documents) {
		this.documents = documents;
	}

	public Integer[] getIdsBank() {
		return idsBank;
	}

	public void setIdsBank(Integer[] idsBank) {
		this.idsBank = idsBank;
	}
}
