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
import javax.persistence.Convert;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.Proxy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.qualshore.etreasury.configure.DateAttributConverter;

/**
 *
 * @author macbookpro
 */
@Entity
@Table(name = "bank_condition")
@JsonIgnoreProperties(ignoreUnknown = false)
@Proxy(lazy = false)
@XmlRootElement

public class BankCondition implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_condition_banque")
    private Integer idConditionBanque;
 
    @Column(name = "libelle_condition")
    private String libelleCondition;
    @Convert(converter = DateAttributConverter.class)
    @Column(name = "date_condition")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCondition;
    @Convert(converter = DateAttributConverter.class)
    @Column(name = "date_debut")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateDebut;
    @Convert(converter = DateAttributConverter.class)
    @Column(name = "date_fin")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFin;
    @Column(name = "is_active")
    private Boolean isActive;
  
    @Column(name = "nom", columnDefinition ="TEXT")
    private String nom;
    @Column(name = "taux_standard")
    private String   tauxStandard;
    
    @Column(name = "nom_document")
    private String   nomDocument;
  
    public String getNomDocument() {
		return nomDocument;
	}

	public void setNomDocument(String nomDocument) {
		this.nomDocument = nomDocument;
	}

	@Column(name = "famille")
    private String famille;

    @Column(name = "categorie")
    private String categorie;

    @Column(name = "specificite")
    private String specificite;
    @Column(name = "negociable")
    private Boolean negociable;
    
    @Column(name = "is_attached_file")
    private Boolean isAttachedFile;
    
    @Column(name = "url_file")
    private String urlFile;
    
    @Column(name = "repertoire_file")
    private String repertoireFile;
    
    public String getRepertoireFile() {
		return repertoireFile;
	}

	public void setRepertoireFile(String repertoireFile) {
		this.repertoireFile = repertoireFile;
	}

	@ManyToOne
    @JoinColumn(name="id_banque")
    private Bank bank;
    
    @ManyToOne
    @JoinColumn(name="id_product")
    private Products product;
    
    public Products getProduct() {
		return product;
	}

	public void setProduct(Products product) {
		this.product = product;
	}

	public BankCondition() {
    }

    public BankCondition(Integer idConditionBanque) {
        this.idConditionBanque = idConditionBanque;
    }
    
    public Bank getBank() {
		return bank;
	}

	public void setBank(Bank bank) {
		this.bank = bank;
	}

    public Boolean getIsAttachedFile() {
		return isAttachedFile;
	}

	public void setIsAttachedFile(Boolean isAttachedFile) {
		this.isAttachedFile = isAttachedFile;
	}

	public String getUrlFile() {
		return urlFile;
	}

	public void setUrlFile(String urlFile) {
		this.urlFile = urlFile;
	}

	public Integer getIdConditionBanque() {
        return idConditionBanque;
    }

    public void setIdConditionBanque(Integer idConditionBanque) {
        this.idConditionBanque = idConditionBanque;
    }

    public String getLibelleCondition() {
        return libelleCondition;
    }

    public void setLibelleCondition(String libelleCondition) {
        this.libelleCondition = libelleCondition;
    }

    public String getTauxStandard() {
		return tauxStandard;
	}

	public void setTauxStandard(String tauxStandard) {
		this.tauxStandard = tauxStandard;
	}

	public Date getDateCondition() {
        return dateCondition;
    }

    public void setDateCondition(Date dateCondition) {
        this.dateCondition = dateCondition;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

 

    public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getFamille() {
        return famille;
    }

    public void setFamille(String famille) {
        this.famille = famille;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getSpecificite() {
        return specificite;
    }

    public void setSpecificite(String specificite) {
        this.specificite = specificite;
    }

    public Boolean getNegociable() {
        return negociable;
    }

    public void setNegociable(Boolean negociable) {
        this.negociable = negociable;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idConditionBanque != null ? idConditionBanque.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BankCondition)) {
            return false;
        }
        BankCondition other = (BankCondition) object;
        if ((this.idConditionBanque == null && other.idConditionBanque != null) || (this.idConditionBanque != null && !this.idConditionBanque.equals(other.idConditionBanque))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.qualshore.etreasury.entity.BankCondition[ idConditionBanque=" + idConditionBanque + " ]";
    }
    
}
