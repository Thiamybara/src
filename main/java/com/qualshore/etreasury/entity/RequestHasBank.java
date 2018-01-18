
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qualshore.etreasury.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Proxy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author macbookpro
 */
@Entity
@Table(name = "request_has_bank")
@JsonIgnoreProperties(ignoreUnknown = false)
@Proxy(lazy = false)
public class RequestHasBank implements Serializable {
	
    private static final long serialVersionUID = 1L;
    
    @EmbeddedId
    RequestHasBankPK requestHasBankPK;    
    
    @JoinColumn(name = "bank_id_banque", referencedColumnName = "id_institution", insertable = false,
    		updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Bank bank;
    
    @JoinColumn(name = "request_id_demande", referencedColumnName = "id_demande", insertable = false,
    		updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Request request;
   
    @Column(name = "date_reception")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateReception;

    @Column(name = "accuse_reception")
    private String accuseReception;

    @Column(name = "has_offer")
    private boolean hasOffer;
    
    public RequestHasBank() {
    }
   
    public Date getDateReception() {
        return dateReception;
    }

    public void setDateReception(Date dateReception) {
        this.dateReception = dateReception;
    }

    public String getAccuseReception() {
        return accuseReception;
    }

    public void setAccuseReception(String accuseReception) {
        this.accuseReception = accuseReception;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

	public RequestHasBankPK getRequestHasBankPK() {
		return requestHasBankPK;
	}

	public void setRequestHasBankPK(RequestHasBankPK requestHasBank) {
		this.requestHasBankPK = requestHasBank;
	}

	public boolean isHasOffer() {
		return hasOffer;
	}

	public void setHasOffer(boolean hasOffer) {
		this.hasOffer = hasOffer;
	}
}
