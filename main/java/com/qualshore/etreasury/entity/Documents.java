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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.Proxy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "documents")
@JsonIgnoreProperties(ignoreUnknown = false)
@Proxy(lazy = false)
@XmlRootElement
public class Documents implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_documents")
    private Integer idDocuments;
    
    @Column(name = "nom")
    private String nom;
    
    @Column(name = "date_document")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateDocument;
    
    @Column(name = "date_chargement")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateChargement;
   
    @Column(name = "repertoire")
    private String repertoire;
    
    @Column(name = "is_active")
    private Boolean isActive;
 
    @Column(name = "mots_cles")
    private String motsCles;

    @Column(name = "etat")
    private String etat;
    
    @Column(name = "is_remove")
    private Boolean isRemove;
    
    @Column(name = "is_private")
    private Boolean isPrivate;
    
    @Column(name = "status_id_status")
    private Integer statusIdStatus;

    @Column(name = "url_document")
    private String urlDocument;
    
    public String getUrlDocument() {
		return urlDocument;
	}

	public void setUrlDocument(String urlDocument) {
		this.urlDocument = urlDocument;
	}
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "documents", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<DocumentsHasUser> documentsHasUserList;
    
    @JoinColumn(name = "category_id_category", referencedColumnName = "id_category")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Category category;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "documents", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<DocumentsHasOffer> documentsHasOfferList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "documents", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<DocumentsHasRequest> documentsHasRequestList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "documents", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<DocumentsHasFolder> documentsHasFolderList;

    public Documents() {
    }

    public Documents(Integer idDocuments) {
        this.idDocuments = idDocuments;
    }

    public Integer getIdDocuments() {
        return idDocuments;
    }

    public void setIdDocuments(Integer idDocuments) {
        this.idDocuments = idDocuments;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Date getDateDocument() {
        return dateDocument;
    }

    public void setDateDocument(Date dateDocument) {
        this.dateDocument = dateDocument;
    }

    public Date getDateChargement() {
        return dateChargement;
    }

    public void setDateChargement(Date dateChargement) {
        this.dateChargement = dateChargement;
    }

    public String getRepertoire() {
        return repertoire;
    }

    public void setRepertoire(String repertoire) {
        this.repertoire = repertoire;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsPrivate() {
		return isPrivate;
	}

	public void setIsPrivate(Boolean isPrivate) {
		this.isPrivate = isPrivate;
	}

	public String getMotsCles() {
        return motsCles;
    }

    public void setMotsCles(String motsCles) {
        this.motsCles = motsCles;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public Boolean getIsRemove() {
        return isRemove;
    }

    public void setIsRemove(Boolean isRemove) {
        this.isRemove = isRemove;
    }

    public Integer getStatusIdStatus() {
        return statusIdStatus;
    }

    public void setStatusIdStatus(Integer statusIdStatus) {
        this.statusIdStatus = statusIdStatus;
    }

    @XmlTransient
    public List<DocumentsHasUser> getDocumentsHasUserList() {
        return documentsHasUserList;
    }

    public void setDocumentsHasUserList(List<DocumentsHasUser> documentsHasUserList) {
        this.documentsHasUserList = documentsHasUserList;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @XmlTransient
    public List<DocumentsHasOffer> getDocumentsHasOfferList() {
        return documentsHasOfferList;
    }

    public void setDocumentsHasOfferList(List<DocumentsHasOffer> documentsHasOfferList) {
        this.documentsHasOfferList = documentsHasOfferList;
    }

    @XmlTransient
    public List<DocumentsHasRequest> getDocumentsHasRequestList() {
        return documentsHasRequestList;
    }

    public void setDocumentsHasRequestList(List<DocumentsHasRequest> documentsHasRequestList) {
        this.documentsHasRequestList = documentsHasRequestList;
    }

    @XmlTransient
    public List<DocumentsHasFolder> getDocumentsHasFolderList() {
        return documentsHasFolderList;
    }

    public void setDocumentsHasFolderList(List<DocumentsHasFolder> documentsHasFolderList) {
        this.documentsHasFolderList = documentsHasFolderList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDocuments != null ? idDocuments.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Documents)) {
            return false;
        }
        Documents other = (Documents) object;
        if ((this.idDocuments == null && other.idDocuments != null) || (this.idDocuments != null && !this.idDocuments.equals(other.idDocuments))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.qualshore.etreasury.entity.Documents[ idDocuments=" + idDocuments + " ]";
    }
    
}
