package com.qualshore.etreasury.model;

import java.io.Serializable;
import java.util.ArrayList;

import com.qualshore.etreasury.entity.CommissionOffer;
import com.qualshore.etreasury.entity.Offer;

public class OfferModel implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Offer offer;
	
	private Integer[] documents;
	
	private ArrayList<CommissionOffer> commissions;

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

	public ArrayList<CommissionOffer> getCommissions() {
		return commissions;
	}

	public void setCommissions(ArrayList<CommissionOffer> commissions) {
		this.commissions = commissions;
	}
}
