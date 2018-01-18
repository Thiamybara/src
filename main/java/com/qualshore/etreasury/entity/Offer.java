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
@Table(name = "offer")
@JsonIgnoreProperties(ignoreUnknown = false)
@Proxy(lazy = false)
@XmlRootElement

public class Offer implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_offre")
    private Integer idOffre;
    
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
  
    @Column(name = "mots_cles")
    private String motsCles;
    
    @Column(name = "taux")
    private String taux;

    @Column(name = "etat")
    private Integer etat;
    
    @Column(name = "is_active")
    private Boolean isActive;
    
    @Column(name = "frais")
    private String frais;
    
    @Column(name = "is_choice_offre")
    private Boolean isChoiceOffre;
    
    @Column(name = "has_document")
    private Boolean hasDocument;
    
    @Column(name = "is_valid")
    private boolean isValid;
    
    @Column(name = "next_validation_group")
    private String nextValidationGroup;
    
    @JoinColumn(name = "demande_id_demande", referencedColumnName = "id_demande")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Request demandeIdDemande;
    
    @JoinColumn(name = "user_banque_id_user_banque", referencedColumnName = "id_utilisateur")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User userBanqueIdUserBanque;
    
    @OneToMany(mappedBy = "offerIdOffre", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Validation> validationList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "offer", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<DocumentsHasOffer> documentsHasOfferList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "offer", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<CommissionOffer> commissionOfferList;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "offer", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ValidationOffer> validationOfferList;
    
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "offer", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ValidationSelect> validationSelectList;

	public Offer() {
    }

    public Offer(Integer idOffre) {
        this.idOffre = idOffre;
    }

    public Integer getIdOffre() {
        return idOffre;
    }

    public void setIdOffre(Integer idOffre) {
        this.idOffre = idOffre;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMotsCles() {
        return motsCles;
    }

    public void setMotsCles(String motsCles) {
        this.motsCles = motsCles;
    }

    public String getTaux() {
        return taux;
    }

    public void setTaux(String taux) {
        this.taux = taux;
    }

    public Integer getEtat() {
		return etat;
	}

	public void setEtat(Integer etat) {
		this.etat = etat;
	}

	public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getFrais() {
        return frais;
    }

    public void setFrais(String frais) {
        this.frais = frais;
    }

    public Boolean getIsChoiceOffre() {
        return isChoiceOffre;
    }

    public void setIsChoiceOffre(Boolean isChoiceOffre) {
        this.isChoiceOffre = isChoiceOffre;
    }

    public Boolean getHasDocument() {
		return hasDocument;
	}

	public void setHasDocument(Boolean hasDocument) {
		this.hasDocument = hasDocument;
	}

	public boolean isValid() {
		return isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

    public String getNextValidationGroup() {
		return nextValidationGroup;
	}

	public void setNextValidationGroup(String nextValidationGroup) {
		this.nextValidationGroup = nextValidationGroup;
	}

	public Request getDemandeIdDemande() {
        return demandeIdDemande;
    }

    public void setDemandeIdDemande(Request demandeIdDemande) {
        this.demandeIdDemande = demandeIdDemande;
    }

    public User getUserBanqueIdUserBanque() {
        return userBanqueIdUserBanque;
    }

    public void setUserBanqueIdUserBanque(User userBanqueIdUserBanque) {
        this.userBanqueIdUserBanque = userBanqueIdUserBanque;
    }

    @XmlTransient
    public List<Validation> getValidationList() {
        return validationList;
    }

    public void setValidationList(List<Validation> validationList) {
        this.validationList = validationList;
    }

    @XmlTransient
    public List<DocumentsHasOffer> getDocumentsHasOfferList() {
        return documentsHasOfferList;
    }

    public void setDocumentsHasOfferList(List<DocumentsHasOffer> documentsHasOfferList) {
        this.documentsHasOfferList = documentsHasOfferList;
    }

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idOffre != null ? idOffre.hashCode() : 0);
        return hash;
    }

	@XmlTransient
    public List<CommissionOffer> getCommissionOfferList() {
		return commissionOfferList;
	}

	public void setCommissionOfferList(List<CommissionOffer> commissionOfferList) {
		this.commissionOfferList = commissionOfferList;
	}

	@Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Offer)) {
            return false;
        }
        Offer other = (Offer) object;
        if ((this.idOffre == null && other.idOffre != null) || (this.idOffre != null && !this.idOffre.equals(other.idOffre))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.qualshore.etreasury.entity.Offer[ idOffre=" + idOffre + " ]";
    }
    
    public List<ValidationOffer> getValidationOfferList() {
		return validationOfferList;
	}

	public void setValidationOfferList(List<ValidationOffer> validationOfferList) {
		this.validationOfferList = validationOfferList;
	}

	public List<ValidationSelect> getValidationSelectList() {
		return validationSelectList;
	}

	public void setValidationSelectList(List<ValidationSelect> validationSelectList) {
		this.validationSelectList = validationSelectList;
	}
}
