/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qualshore.etreasury.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
 *
 * @author macbookpro
 */
@Embeddable
public class RequestHasBankPK implements Serializable {
	
    @Column(name = "request_id_demande")
    private int requestIdDemande;
    
    @Column(name = "bank_id_banque")
    private int bankIdBanque;
    
    public RequestHasBankPK() {
		super();
	}

	public RequestHasBankPK(int requestIdDemande, int bankIdBanque) {
		super();
		this.requestIdDemande = requestIdDemande;
		this.bankIdBanque = bankIdBanque;
		System.out.println(this.toString());
	}

	public int getRequestIdDemande() {
        return requestIdDemande;
    }

    public void setRequestIdDemande(int requestIdDemande) {
        this.requestIdDemande = requestIdDemande;
    }

    public int getBankIdBanque() {
        return bankIdBanque;
    }

    public void setBankIdBanque(int bankIdBanque) {
        this.bankIdBanque = bankIdBanque;
    }

	@Override
	public String toString() {
		return "RequestHasBankPK [requestIdDemande=" + requestIdDemande + ", bankIdBanque=" + bankIdBanque + "]";
	}
    
    
}
