/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qualshore.etreasury.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.Proxy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author macbookpro
 */
@Entity
@Table(name = "rate_day")
@JsonIgnoreProperties(ignoreUnknown = false)
@Proxy(lazy = false)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RateDay.findAll", query = "SELECT r FROM RateDay r"),
    @NamedQuery(name = "RateDay.findByIdTauxJour", query = "SELECT r FROM RateDay r WHERE r.idTauxJour = :idTauxJour"),
    @NamedQuery(name = "RateDay.findByType", query = "SELECT r FROM RateDay r WHERE r.type = :type"),
    @NamedQuery(name = "RateDay.findByDescription", query = "SELECT r FROM RateDay r WHERE r.description = :description"),
    @NamedQuery(name = "RateDay.findByValeur", query = "SELECT r FROM RateDay r WHERE r.valeur = :valeur"),
    @NamedQuery(name = "RateDay.findByDateValeur", query = "SELECT r FROM RateDay r WHERE r.dateValeur = :dateValeur"),
    @NamedQuery(name = "RateDay.findByDateDebutValidite", query = "SELECT r FROM RateDay r WHERE r.dateDebutValidite = :dateDebutValidite"),
    @NamedQuery(name = "RateDay.findByDateFinValidite", query = "SELECT r FROM RateDay r WHERE r.dateFinValidite = :dateFinValidite"),
    @NamedQuery(name = "RateDay.findByIsActive", query = "SELECT r FROM RateDay r WHERE r.isActive = :isActive"),
    @NamedQuery(name = "RateDay.findByMotsCles", query = "SELECT r FROM RateDay r WHERE r.motsCles = :motsCles"),
    @NamedQuery(name = "RateDay.findByMontant", query = "SELECT r FROM RateDay r WHERE r.montant = :montant"),
    @NamedQuery(name = "RateDay.findByEcheance", query = "SELECT r FROM RateDay r WHERE r.echeance = :echeance"),
    @NamedQuery(name = "RateDay.findByTauxMax", query = "SELECT r FROM RateDay r WHERE r.tauxMax = :tauxMax")})
public class RateDay implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_taux_jour")
    private Integer idTauxJour;

    @Column(name = "type")
    private String type;

    @Column(name = "description")
    private String description;

    @Column(name = "valeur")
    private String valeur;
    
    @Column(name = "date_valeur")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateValeur;
    
    @Column(name = "date_debut_validite")
    @Temporal(TemporalType.DATE)
    private Date dateDebutValidite;
    
    @Column(name = "date_fin_validite")
    @Temporal(TemporalType.DATE)
    private Date dateFinValidite;
    
    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "mots_cles")
    private String motsCles;

    @Column(name = "montant")
    private String montant;

    @Column(name = "echeance")
    private String echeance;

    @Column(name = "taux_max")
    private String tauxMax;
    
    @Column(name = "duree")
    private String duree;
    
    @Column(name = "achat")
    private String achat;
    
    @Column(name = "vente")
    private String vente;
    
    //@Column(name = "devise")
    //private String devise;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "devise")
    private Devise devise;

	@JoinColumn(name = "produits_id_produits", referencedColumnName = "id_produits")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Products produitsIdProduits;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tauxJourIdTauxJour", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Commission> commissionList;
    
    @JoinColumn(name = "id_banque", referencedColumnName = "id_institution")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Bank bank;

    public RateDay() {
    }

	public RateDay(Integer idTauxJour, String type, String description, String valeur, Date dateValeur,
			Date dateDebutValidite, Date dateFinValidite, Boolean isActive, String motsCles, String montant,
			String echeance, String tauxMax, String duree, String achat, String vente, Devise devise,
			Products produitsIdProduits, List<Commission> commissionList, Bank bank) {
		super();
		this.idTauxJour = idTauxJour;
		this.type = type;
		this.description = description;
		this.valeur = valeur;
		this.dateValeur = dateValeur;
		this.dateDebutValidite = dateDebutValidite;
		this.dateFinValidite = dateFinValidite;
		this.isActive = isActive;
		this.motsCles = motsCles;
		this.montant = montant;
		this.echeance = echeance;
		this.tauxMax = tauxMax;
		this.duree = duree;
		this.achat = achat;
		this.vente = vente;
		this.devise = devise;
		this.produitsIdProduits = produitsIdProduits;
		this.commissionList = commissionList;
		this.bank = bank;
	}

	public String getVente() {
		return vente;
	}

	public void setVente(String vente) {
		this.vente = vente;
	}

	public String getAchat() {
		return achat;
	}

	public void setAchat(String achat) {
		this.achat = achat;
	}
	
    public RateDay(Integer idTauxJour) {
        this.idTauxJour = idTauxJour;
    }

    public Integer getIdTauxJour() {
        return idTauxJour;
    }

    public void setIdTauxJour(Integer idTauxJour) {
        this.idTauxJour = idTauxJour;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValeur() {
        return valeur;
    }

    public void setValeur(String valeur) {
        this.valeur = valeur;
    }

    public Date getDateValeur() {
        return dateValeur;
    }

    public void setDateValeur(Date dateValeur) {
        this.dateValeur = dateValeur;
    }

    public Date getDateDebutValidite() {
        return dateDebutValidite;
    }

    public void setDateDebutValidite(Date dateDebutValidite) {
        this.dateDebutValidite = dateDebutValidite;
    }

    public Date getDateFinValidite() {
        return dateFinValidite;
    }

    public void setDateFinValidite(Date dateFinValidite) {
        this.dateFinValidite = dateFinValidite;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getMotsCles() {
        return motsCles;
    }

    public void setMotsCles(String motsCles) {
        this.motsCles = motsCles;
    }

    public String getMontant() {
        return montant;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public String getEcheance() {
        return echeance;
    }

    public void setEcheance(String echeance) {
        this.echeance = echeance;
    }

    public String getTauxMax() {
        return tauxMax;
    }

    public void setTauxMax(String tauxMax) {
        this.tauxMax = tauxMax;
    }
    public String getDuree() {
 		return duree;
 	}

 	public void setDuree(String duree) {
 		this.duree = duree;
 	}
    public Products getProduitsIdProduits() {
        return produitsIdProduits;
    }

    public void setProduitsIdProduits(Products produitsIdProduits) {
        this.produitsIdProduits = produitsIdProduits;
    }

    @XmlTransient
    public List<Commission> getCommissionList() {
        return commissionList;
    }

    public void setCommissionList(List<Commission> commissionList) {
        this.commissionList = commissionList;
    }
    
    

    public Bank getBank() {
		return bank;
	}

	public void setBank(Bank bank) {
		this.bank = bank;
	}
	
	public Devise getDevise() {
		return devise;
	}

	public void setDevise(Devise devise) {
		this.devise = devise;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idTauxJour != null ? idTauxJour.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RateDay)) {
            return false;
        }
        RateDay other = (RateDay) object;
        if ((this.idTauxJour == null && other.idTauxJour != null) || (this.idTauxJour != null && !this.idTauxJour.equals(other.idTauxJour))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.qualshore.etreasury.entity.RateDay[ idTauxJour=" + idTauxJour + " ]";
    }   
}