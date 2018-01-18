package com.qualshore.etreasury.model;

import com.qualshore.etreasury.entity.RequestHasBank;

public class OfferRequestModel {

	private RequestHasBank requestHB;
	private OfferModel offerModel;
	
	public OfferRequestModel() {
	}

	public RequestHasBank getRequestHB() {
		return requestHB;
	}

	public void setRequestHB(RequestHasBank requestHB) {
		this.requestHB = requestHB;
	}

	public OfferModel getOfferModel() {
		return offerModel;
	}

	public void setOfferModel(OfferModel offerModel) {
		this.offerModel = offerModel;
	}
}
