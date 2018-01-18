/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qualshore.etreasury.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Converter;
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
@Table(name = "request")
@JsonIgnoreProperties(ignoreUnknown = false)
@Proxy(lazy = false)
@XmlRootElement
public class Request implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_demande")
    private Integer idDemande;
    
    @Column(name = "date_valeur")
    @Temporal(TemporalType.TIMESTAMP)
    @Convert(converter = DateAttributConverter.class)
    private Date dateValeur;
    
    @Column(name = "date_maturite")
    @Temporal(TemporalType.TIMESTAMP)
    @Convert(converter = DateAttributConverter.class)
    private Date dateMaturite;
    
    @Column(name = "taux_min")
    private String tauxMin;
    
    @Column(name = "taux_max")
    private String tauxMax;
    
    @Column(name = "mots_cles")
    private String motsCles;
    
    @Column(name = "etat")
    private Integer etat;
    
    @Column(name = "is_active")
    private Boolean isActive;
    
    @Column(name = "montant")
    private String montant;
    
    @Column(name = "montant_xof")
    private String montantXof;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "devise")
    private Devise devise;
    
    @Column(name = "numero_request")
    private String numeroRequest;
    
   
	@Column(name = "date_debut_demande")
    @Temporal(TemporalType.TIMESTAMP)
    @Convert(converter = DateAttributConverter.class)
    private Date dateDebutDemande;
    
    @Column(name = "date_fin_demande")
    @Temporal(TemporalType.TIMESTAMP)
    @Convert(converter = DateAttributConverter.class)
    private Date dateFinDemande;
    
    @Column(name = "date_escompte")
    @Temporal(TemporalType.TIMESTAMP)
    @Convert(converter = DateAttributConverter.class)
    private Date dateEscompte;
    
	@Column(name = "is_enchere_anonyme")
    private Boolean isEnchereAnonyme;
    
    @Column(name = "is_enchere_multiple")
    private Boolean isEnchereMultiple;

    @Column(name = "accuse")
    private String accuse;

    @Column(name = "type_cotation")
    private String typeCotation;
    
    @Column(name = "cours_max")
    private String coursMax;
   
    @Column(name = "cours_min")
    private String coursMin;

    @Column(name = "ref_cours")
    private String refCours;
    
    @Column(name = "marge")
    private String marge;
    
    @Column(name = "cours")
    private String cours;
    
    @Column(name = "type_change")
    private String typeChange;
    
    @Column(name = "sens")
    private String sens;
    
    @Column(name = "date_negociation")
    private String dateNegociation;
    
    @Column(name = "devise_entree")
    private String deviseEntree;
       
    @Column(name = "devise_sortie")
    private String deviseSortie;
    
    @Column(name = "contre_valeur")
    private String contreValeur;
    
    @Column(name = "taux_cbeao")
    private String tauxCbeao;
    
    @Column(name = "donneur_ordre")
    private String donneurOrdre;
    
    @Column(name = "beneficiaire")
    private String beneficiaire;
    
    @Column(name = "typre_credit")
    private String typreCredit;
    
    @Column(name = "mode_reglement")
    private String modeReglement;
    
    @Column(name = "tarification")
    private String tarification;
    
    @Column(name = "forme_rd")
    private String formeRd;
    
    @Column(name = "taux_tthu")
    private String tauxTthu;
    
    @Column(name = "date_echeance")
    @Temporal(TemporalType.TIMESTAMP)
    @Convert(converter = DateAttributConverter.class)
    private Date dateEcheance;
    
    @Column(name = "type_taux")
    private String typeTaux;
    
    @Column(name = "tire")
    private String tire;

    @Column(name = "duree")
    private String duree;
    
	@Column(name = "tireur")
    private String tireur;
    
    @Column(name = "has_document")
    private Boolean hasDocument;
    
    @Column(name = "products_famille")
    private String productsFamille;
    
    @Column(name = "is_valid")
    private Boolean isValid;
    
    @Column(name = "next_validation_group")
    private String nextValidationGroup;
    
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_entreprise")
    private UserEntreprise userEntreprise;
    
    @JoinColumn(name = "product")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Products product;  
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "demandeIdDemande", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Offer> offerList;
    
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "request")
    @JsonIgnore
    private List<RequestHasBank> requestHasBankList = new ArrayList<>();
    
    @OneToMany(mappedBy = "requestIdDemande", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Validation> validationList;
    
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "request", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<DocumentsHasRequest> documentsHasRequestList;
    
    @OneToMany(cascade = CascadeType.REMOVE,mappedBy = "request", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ValidationRequest> validationRequestList;
    
    public List<ValidationRequest> getValidationRequestList() {
		return validationRequestList;
	}

	public void setValidationRequestList(List<ValidationRequest> validationRequestList) {
		this.validationRequestList = validationRequestList;
	}

	public Request() {
    }

    public Request(Integer idDemande) {
        this.idDemande = idDemande;
    }

    public Integer getIdDemande() {
        return idDemande;
    }

    public void setIdDemande(Integer idDemande) {
        this.idDemande = idDemande;
    }
    public String getNumeroRequest() {
		return numeroRequest;
	}

	public void setNumeroRequest(String numeroRequest) {
		this.numeroRequest = numeroRequest;
	}

    public Date getDateValeur() {
        return dateValeur;
    }

    public void setDateValeur(Date dateValeur) {
        this.dateValeur = dateValeur;
    }

    public Date getDateMaturite() {
        return dateMaturite;
    }

    public void setDateMaturite(Date dateMaturite) {
        this.dateMaturite = dateMaturite;
    }

    public String getTauxMin() {
        return tauxMin;
    }

    public void setTauxMin(String tauxMin) {
        this.tauxMin = tauxMin;
    }

    public String getTauxMax() {
        return tauxMax;
    }

    public void setTauxMax(String tauxMax) {
        this.tauxMax = tauxMax;
    }

    public String getMotsCles() {
        return motsCles;
    }

    public void setMotsCles(String motsCles) {
        this.motsCles = motsCles;
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

    public String getMontant() {
        return montant;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public String getMontantXof() {
        return montantXof;
    }

    public void setMontantXof(String montantXof) {
        this.montantXof = montantXof;
    }

    public Devise getDevise() {
        return devise;
    }

    public void setDevise(Devise devise) {
        this.devise = devise;
    }

    public Date getDateDebutDemande() {
        return dateDebutDemande;
    }

    public void setDateDebutDemande(Date dateDebutDemande) {
        this.dateDebutDemande = dateDebutDemande;
    }

    public Date getDateFinDemande() {
        return dateFinDemande;
    }

    public void setDateFinDemande(Date dateFinDemande) {
        this.dateFinDemande = dateFinDemande;
    }
    public Date getDateEscompte() {
		return dateEscompte;
	}

	public void setDateEscompte(Date dateEscompte) {
		this.dateEscompte = dateEscompte;
	}


    public Boolean getIsEnchereAnonyme() {
        return isEnchereAnonyme;
    }

    public void setIsEnchereAnonyme(Boolean isEnchereAnonyme) {
        this.isEnchereAnonyme = isEnchereAnonyme;
    }

    public Boolean getIsEnchereMultiple() {
        return isEnchereMultiple;
    }

    public void setIsEnchereMultiple(Boolean isEnchereMultiple) {
        this.isEnchereMultiple = isEnchereMultiple;
    }

    public String getAccuse() {
        return accuse;
    }

    public void setAccuse(String accuse) {
        this.accuse = accuse;
    }

    public String getTypeCotation() {
        return typeCotation;
    }

    public void setTypeCotation(String typeCotation) {
        this.typeCotation = typeCotation;
    }

    public String getCoursMax() {
        return coursMax;
    }

    public void setCoursMax(String coursMax) {
        this.coursMax = coursMax;
    }

    public String getCoursMin() {
        return coursMin;
    }

    public void setCoursMin(String coursMin) {
        this.coursMin = coursMin;
    }

    public String getRefCours() {
        return refCours;
    }

    public void setRefCours(String refCours) {
        this.refCours = refCours;
    }

    public String getMarge() {
        return marge;
    }

    public void setMarge(String marge) {
        this.marge = marge;
    }

    public String getCours() {
        return cours;
    }

    public void setCours(String cours) {
        this.cours = cours;
    }

    public String getTypeChange() {
        return typeChange;
    }

    public void setTypeChange(String typeChange) {
        this.typeChange = typeChange;
    }

    public String getSens() {
        return sens;
    }

    public void setSens(String sens) {
        this.sens = sens;
    }

    public String getDateNegociation() {
        return dateNegociation;
    }

    public void setDateNegociation(String dateNegociation) {
        this.dateNegociation = dateNegociation;
    }

    public String getDeviseEntree() {
        return deviseEntree;
    }

    public void setDeviseEntree(String deviseEntree) {
        this.deviseEntree = deviseEntree;
    }

    public String getDeviseSortie() {
        return deviseSortie;
    }

    public void setDeviseSortie(String deviseSortie) {
        this.deviseSortie = deviseSortie;
    }

    public String getContreValeur() {
        return contreValeur;
    }

    public void setContreValeur(String contreValeur) {
        this.contreValeur = contreValeur;
    }

    public String getTauxCbeao() {
        return tauxCbeao;
    }

    public void setTauxCbeao(String tauxCbeao) {
        this.tauxCbeao = tauxCbeao;
    }

    public String getDonneurOrdre() {
        return donneurOrdre;
    }

    public void setDonneurOrdre(String donneurOrdre) {
        this.donneurOrdre = donneurOrdre;
    }

    public String getBeneficiaire() {
        return beneficiaire;
    }

    public void setBeneficiaire(String beneficiaire) {
        this.beneficiaire = beneficiaire;
    }

    public String getTypreCredit() {
        return typreCredit;
    }

    public void setTypreCredit(String typreCredit) {
        this.typreCredit = typreCredit;
    }

    public String getModeReglement() {
        return modeReglement;
    }

    public void setModeReglement(String modeReglement) {
        this.modeReglement = modeReglement;
    }

    public String getTarification() {
        return tarification;
    }

    public void setTarification(String tarification) {
        this.tarification = tarification;
    }

    public String getFormeRd() {
        return formeRd;
    }

    public void setFormeRd(String formeRd) {
        this.formeRd = formeRd;
    }

    public String getTauxTthu() {
        return tauxTthu;
    }

    public void setTauxTthu(String tauxTthu) {
        this.tauxTthu = tauxTthu;
    }

    public Date getDateEcheance() {
        return dateEcheance;
    }

    public void setDateEcheance(Date dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    public String getTypeTaux() {
        return typeTaux;
    }

    public void setTypeTaux(String typeTaux) {
        this.typeTaux = typeTaux;
    }

   
    public String getTire() {
		return tire;
	}

	public void setTire(String tire) {
		this.tire = tire;
	}

	public String getDuree() {
		return duree;
	}

	public void setDuree(String duree) {
		this.duree = duree;
	}
    public String getTireur() {
        return tireur;
    }

    public void setTireur(String tireur) {
        this.tireur = tireur;
    }

    public Boolean getHasDocument() {
		return hasDocument;
	}

	public void setHasDocument(Boolean hasDocument) {
		this.hasDocument = hasDocument;
	}

    public String getProductsFamille() {
        return productsFamille;
    }

    public void setProductsFamille(String productsFamille) {
        this.productsFamille = productsFamille;
    }

    public Boolean getIsValid() {
        return isValid;
    }

    public void setIsValid(Boolean isValid) {
        this.isValid = isValid;
    }
    
    public String getNextValidationGroup() {
		return nextValidationGroup;
	}

	public void setNextValidationGroup(String nextValidationGroup) {
		this.nextValidationGroup = nextValidationGroup;
	}

	public User getUserEntreprise() {
        return userEntreprise;
    }

    public void setUserEntreprise(UserEntreprise userEntreprise) {
        this.userEntreprise = userEntreprise;
    }

    public Products getProduct() {
        return product;
    }

    public void setProduct(Products product) {
        this.product = product;
    }

    @XmlTransient
    public List<Offer> getOfferList() {
        return offerList;
    }

    public void setOfferList(List<Offer> offerList) {
        this.offerList = offerList;
    }

    @XmlTransient
    public List<RequestHasBank> getRequestHasBankList() {
        return requestHasBankList;
    }

    public void setRequestHasBankList(List<RequestHasBank> requestHasBankList) {
        this.requestHasBankList = requestHasBankList;
    }

    @XmlTransient
    public List<Validation> getValidationList() {
        return validationList;
    }

    public void setValidationList(List<Validation> validationList) {
        this.validationList = validationList;
    }

    @XmlTransient
    public List<DocumentsHasRequest> getDocumentsHasRequestList() {
        return documentsHasRequestList;
    }

    public void setDocumentsHasRequestList(List<DocumentsHasRequest> documentsHasRequestList) {
        this.documentsHasRequestList = documentsHasRequestList;
    }

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idDemande != null ? idDemande.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Request)) {
            return false;
        }
        Request other = (Request) object;
        if ((this.idDemande == null && other.idDemande != null) || (this.idDemande != null && !this.idDemande.equals(other.idDemande))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.qualshore.etreasury.entity.Request[ idDemande=" + idDemande + " ]";
    }
    
    public RequestHasBank addRequestHasBank(Bank bank) {
		
    	RequestHasBank rHB = new RequestHasBank();
    	//rHB.setBankIdBanque(bank.getIdInstitution());
    	//rHB.setRequestIdDemande(this.getIdDemande());
    	rHB.setRequestHasBankPK(new RequestHasBankPK(this.getIdDemande(), bank.getIdInstitution()));
    	
    	rHB.setBank(bank);
    	rHB.setRequest(this);
    	bank.getRequestHasBankList().add(rHB);
    	this.getRequestHasBankList().add(rHB);
    	
    	return rHB;
	}
}
