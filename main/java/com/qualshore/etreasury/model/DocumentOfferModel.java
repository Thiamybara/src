package com.qualshore.etreasury.model;

import java.io.Serializable;

import com.qualshore.etreasury.entity.Offer;

public class DocumentOfferModel implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Offer offer;
	
	private Integer[] documents;

	public Offer getOffer() {
		return offer;
	}

	public void setOffer(Offer offer) {
		this.offer = offer;
	}

	public Integer[] getDocuments() {
		return documents;
	}

	public void setDocuments(Integer[] documents) {
		this.documents = documents;
	}
}
