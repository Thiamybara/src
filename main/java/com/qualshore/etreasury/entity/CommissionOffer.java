package com.qualshore.etreasury.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Proxy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "commission_offer")
@JsonIgnoreProperties(ignoreUnknown = false)
@Proxy(lazy = false)
@XmlRootElement

public class CommissionOffer implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_commission_offer")
	private Integer idCommissionOffer;

    @Column(name = "type")
	private String type;
	

    @Column(name = "nature")
	private String nature;

    @Column(name = "valeur")
	private String valeur;
	
    @Column(name = "detail")
	private String detail;
	
	@JoinColumn(name = "offer", referencedColumnName = "id_offre")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Offer offer;

	public CommissionOffer() {
	}

	public Integer getIdCommissionOffer() {
		return idCommissionOffer;
	}

	public void setIdCommissionOffer(Integer idCommissionOffer) {
		this.idCommissionOffer = idCommissionOffer;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getNature() {
		return nature;
	}

	public void setNature(String nature) {
		this.nature = nature;
	}

	public String getValeur() {
		return valeur;
	}

	public void setValeur(String valeur) {
		this.valeur = valeur;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public Offer getOffer() {
		return offer;
	}

	public void setOffer(Offer offer) {
		this.offer = offer;
	}
}
