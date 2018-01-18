package com.qualshore.etreasury.model;

import com.qualshore.etreasury.entity.Offer;
import com.qualshore.etreasury.entity.RequestHasBank;

public class OfferModelValidation {

	private RequestHasBank requestHasBank;
	private Offer offer;
	
	public OfferModelValidation() {
	}

	public RequestHasBank getRequestHasBank() {
		return requestHasBank;
	}

	public void setRequestHasBank(RequestHasBank requestHasBank) {
		this.requestHasBank = requestHasBank;
	}

	public Offer getOffer() {
		return offer;
	}

	public void setOffer(Offer offer) {
		this.offer = offer;
	}
}
