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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
@Table(name = "products")
@JsonIgnoreProperties(ignoreUnknown = false)
@Proxy(lazy = false)
@XmlRootElement

public class Products implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_produits")
    private Integer idProduits;

    @Column(name = "nom")
    private String nom;

    @Column(name = "description")
    private String description;

    @Column(name = "avantages")
    private String avantages;

    @Column(name = "inconvenients")
    private String inconvenients;

    @Column(name = "famille")
    private String famille;

    @Column(name = "standard")
    private String standard;

    @Column(name = "non_negociable")
    private String nonNegociable;

    @Column(name = "mots_cles")
    private String motsCles;
    
    @Column(name = "is_actif")
    private Boolean isActif;

    @Column(name = "photo")
    private String photo;

    @Column(name = "devise_entree")
    private String deviseEntree;

    @Column(name = "devise_sortie")
    private String deviseSortie;

    @Column(name = "sens")
    private String sens;
    @Column(name = "is_for_banque")
    private Boolean isForBanque;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Convert(converter = DateAttributConverter.class)
    @Column(name = "date")
    private Date date;
    
    @Column(name = "is_attached_file")
    private Boolean isAttachedFile;

    @Column(name = "repertoire")
    private String repertoire;
    
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade= CascadeType.ALL)
    @JsonIgnore
    private List<Request> requestList;    
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "produitsIdProduits", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<RateDay> rateDayList;
    
    @OneToMany(mappedBy="product", cascade= CascadeType.ALL, fetch= FetchType.LAZY)
    @JsonIgnore
    private List<BankCondition> bankConditions;
    
   
    public List<BankCondition> getBankConditions() {
		return bankConditions;
	}

	public void setBankConditions(List<BankCondition> bankConditions) {
		this.bankConditions = bankConditions;
	}

	@ManyToOne()
    @JoinColumn(name = "id_type_produit")
    private ProductType productType;    

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "product", fetch = FetchType.LAZY)
	@JsonIgnore
	private List<ValidationLevel> validationLevelList;
	
	@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "products")
	@JsonIgnore
    private List<Notifications> notificationsList;

	/*@ManyToMany(mappedBy = "products")
	@JsonIgnore
    private List<Bank> banks = new ArrayList<>();*/

	public Products() {
    }

    public Products(Integer idProduits) {
        this.idProduits = idProduits;
    }
    
    public ProductType getProductType() {
		return productType;
	}

	public void setProductType(ProductType productType) {
		this.productType = productType;
	}

    public Integer getIdProduits() {
        return idProduits;
    }

    public void setIdProduits(Integer idProduits) {
        this.idProduits = idProduits;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAvantages() {
        return avantages;
    }

    public void setAvantages(String avantages) {
        this.avantages = avantages;
    }

    public String getInconvenients() {
        return inconvenients;
    }

    public void setInconvenients(String inconvenients) {
        this.inconvenients = inconvenients;
    }

    public String getFamille() {
        return famille;
    }

    public void setFamille(String famille) {
        this.famille = famille;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public String getNonNegociable() {
        return nonNegociable;
    }

    public void setNonNegociable(String nonNegociable) {
        this.nonNegociable = nonNegociable;
    }

    public String getMotsCles() {
        return motsCles;
    }

    public void setMotsCles(String motsCles) {
        this.motsCles = motsCles;
    }

    public Boolean getIsActif() {
        return isActif;
    }

    public void setIsActif(Boolean isActif) {
        this.isActif = isActif;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
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

    public String getSens() {
        return sens;
    }

    public void setSens(String sens) {
        this.sens = sens;
    }

    public Boolean getIsForBanque() {
        return isForBanque;
    }

    public void setIsForBanque(Boolean isForBanque) {
        this.isForBanque = isForBanque;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Boolean getIsAttachedFile() {
        return isAttachedFile;
    }

    public void setIsAttachedFile(Boolean isAttachedFile) {
        this.isAttachedFile = isAttachedFile;
    }

    public String getRepertoire() {
        return repertoire;
    }

    public void setRepertoire(String repertoire) {
        this.repertoire = repertoire;
    }

    
    public List<Request> getRequestList() {
        return requestList;
    }

    public void setRequestList(List<Request> requestList) {
        this.requestList = requestList;
    }

    @XmlTransient
    public List<RateDay> getRateDayList() {
        return rateDayList;
    }

    public void setRateDayList(List<RateDay> rateDayList) {
        this.rateDayList = rateDayList;
    }

    @XmlTransient
    public List<ValidationLevel> getValidationLevelList() {
        return validationLevelList;
    }

    public void setValidationLevelList(List<ValidationLevel> validationLevelList) {
        this.validationLevelList = validationLevelList;
    }
    @XmlTransient
    public List<Notifications> getNotificationsList() {
        return notificationsList;
    }

    public void setNotificationsList(List<Notifications> notificationsList) {
        this.notificationsList = notificationsList;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idProduits != null ? idProduits.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Products)) {
            return false;
        }
        Products other = (Products) object;
        if ((this.idProduits == null && other.idProduits != null) || (this.idProduits != null && !this.idProduits.equals(other.idProduits))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.qualshore.etreasury.entity.Products[ idProduits=" + idProduits + " ]";
    }
    
}
