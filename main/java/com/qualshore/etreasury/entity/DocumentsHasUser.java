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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@Table(name = "documents_has_user")
@JsonIgnoreProperties(ignoreUnknown = false)
@Proxy(lazy = false)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DocumentsHasUser.findAll", query = "SELECT d FROM DocumentsHasUser d"),
    @NamedQuery(name = "DocumentsHasUser.findByDocumentsIdDocuments", query = "SELECT d FROM DocumentsHasUser d WHERE d.documentsHasUserPK.documentsIdDocuments = :documentsIdDocuments"),
    @NamedQuery(name = "DocumentsHasUser.findByUserIdUtilisateur", query = "SELECT d FROM DocumentsHasUser d WHERE d.documentsHasUserPK.userIdUtilisateur = :userIdUtilisateur"),
    @NamedQuery(name = "DocumentsHasUser.findByStatusDoc", query = "SELECT d FROM DocumentsHasUser d WHERE d.statusDoc = :statusDoc")})
public class DocumentsHasUser implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected DocumentsHasUserPK documentsHasUserPK;
    @Column(name = "status_doc")
    private Boolean statusDoc;
    @Column(name = "date_publication")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datePublication;
    

	@JoinColumn(name = "documents_id_documents", referencedColumnName = "id_documents", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Documents documents;
    @JoinColumn(name = "user_id_utilisateur", referencedColumnName = "id_utilisateur", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User user;

    public DocumentsHasUser() {
    }

    public DocumentsHasUser(DocumentsHasUserPK documentsHasUserPK) {
        this.documentsHasUserPK = documentsHasUserPK;
    }

    public DocumentsHasUser(int documentsIdDocuments, int userIdUtilisateur) {
        this.documentsHasUserPK = new DocumentsHasUserPK(documentsIdDocuments, userIdUtilisateur);
    }

    public DocumentsHasUser(DocumentsHasUserPK dHUPK, Documents doc, User user) {
		this.documentsHasUserPK = dHUPK;
		this.documents = doc;
		this.user = user;
	}

	public DocumentsHasUserPK getDocumentsHasUserPK() {
        return documentsHasUserPK;
    }

    public void setDocumentsHasUserPK(DocumentsHasUserPK documentsHasUserPK) {
        this.documentsHasUserPK = documentsHasUserPK;
    }

    public Boolean getStatusDoc() {
        return statusDoc;
    }

    public void setStatusDoc(Boolean statusDoc) {
        this.statusDoc = statusDoc;
    }
    public Date getDatePublication() {
		return datePublication;
	}

	public void setDatePublication(Date datePublication) {
		this.datePublication = datePublication;
	}
    public Documents getDocuments() {
        return documents;
    }

    public void setDocuments(Documents documents) {
        this.documents = documents;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (documentsHasUserPK != null ? documentsHasUserPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DocumentsHasUser)) {
            return false;
        }
        DocumentsHasUser other = (DocumentsHasUser) object;
        if ((this.documentsHasUserPK == null && other.documentsHasUserPK != null) || (this.documentsHasUserPK != null && !this.documentsHasUserPK.equals(other.documentsHasUserPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.qualshore.etreasury.entity.DocumentsHasUser[ documentsHasUserPK=" + documentsHasUserPK + " ]";
    }
    
}
