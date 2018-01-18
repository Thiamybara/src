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
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Proxy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author macbookpro
 */
@Entity
@Table(name = "validation_request")
@JsonIgnoreProperties(ignoreUnknown = false)
@Proxy(lazy = false)
@XmlRootElement

public class ValidationRequest implements Serializable {
	
    private static final long serialVersionUID = 1L;

    @EmbeddedId
	protected ValidationRequestPK ValidationRequestPK;
    
    @JoinColumn(name = "user", referencedColumnName = "id_utilisateur")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User user;
    
    @JoinColumn(name = "request", referencedColumnName = "id_demande", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Request request;
    
    @Column(name = "niveau", insertable = false, updatable = false)
    private Integer niveau;
    
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    
    @Column(name = "status")
    private Integer status;
    
	public ValidationRequest() {
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getNiveau() {
		return niveau;
	}

	public void setNiveau(Integer niveau) {
		this.niveau = niveau;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;
	}

	public ValidationRequestPK getValidationRequestPK() {
		return ValidationRequestPK;
	}

	public void setValidationRequestPK(ValidationRequestPK validationRequestPK) {
		ValidationRequestPK = validationRequestPK;
	}
}
