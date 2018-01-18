package com.qualshore.etreasury.model;


import java.io.Serializable;
import java.util.ArrayList;

import com.qualshore.etreasury.entity.Bank;
import com.qualshore.etreasury.entity.Documents;
import com.qualshore.etreasury.entity.Request;

public class RequestModelDetails implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Request request;
    
    private OfferModel offerModel;
    
    private ArrayList<Documents> documents;
    
    private ArrayList<Bank> idsBank;
    
    public RequestModelDetails() {
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

	public OfferModel getOfferModel() {
		return offerModel;
	}

	public void setOfferModel(OfferModel offerModel) {
		this.offerModel = offerModel;
	}

	public ArrayList<Documents> getDocuments() {
        return documents;
    }

    public void setDocuments(ArrayList<Documents> documents) {
        this.documents = documents;
    }

    public ArrayList<Bank> getIdsBank() {
        return idsBank;
    }

    public void setIdsBank(ArrayList<Bank> idsBank) {
        this.idsBank = idsBank;
    }
}