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
@Table(name = "documents_has_folder")
@JsonIgnoreProperties(ignoreUnknown = false)
@Proxy(lazy = false)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DocumentsHasFolder.findAll", query = "SELECT d FROM DocumentsHasFolder d"),
    @NamedQuery(name = "DocumentsHasFolder.findByDocumentsIdDocuments", query = "SELECT d FROM DocumentsHasFolder d WHERE d.documentsHasFolderPK.documentsIdDocuments = :documentsIdDocuments"),
    @NamedQuery(name = "DocumentsHasFolder.findByDossierIdDossier", query = "SELECT d FROM DocumentsHasFolder d WHERE d.documentsHasFolderPK.dossierIdDossier = :dossierIdDossier"),
    @NamedQuery(name = "DocumentsHasFolder.findByDate", query = "SELECT d FROM DocumentsHasFolder d WHERE d.date = :date"),
    @NamedQuery(name = "DocumentsHasFolder.findByEtat", query = "SELECT d FROM DocumentsHasFolder d WHERE d.etat = :etat")})
public class DocumentsHasFolder implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected DocumentsHasFolderPK documentsHasFolderPK;
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Column(name = "etat")
    private Boolean etat;
    @JoinColumn(name = "documents_id_documents", referencedColumnName = "id_documents", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Documents documents;
    /*@JoinColumn(name = "dossier_id_dossier", referencedColumnName = "id_category", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Category category;*/

    public DocumentsHasFolder() {
    }

    public DocumentsHasFolder(DocumentsHasFolderPK documentsHasFolderPK) {
        this.documentsHasFolderPK = documentsHasFolderPK;
    }

    public DocumentsHasFolder(int documentsIdDocuments, int dossierIdDossier) {
        this.documentsHasFolderPK = new DocumentsHasFolderPK(documentsIdDocuments, dossierIdDossier);
    }

    public DocumentsHasFolderPK getDocumentsHasFolderPK() {
        return documentsHasFolderPK;
    }

    public void setDocumentsHasFolderPK(DocumentsHasFolderPK documentsHasFolderPK) {
        this.documentsHasFolderPK = documentsHasFolderPK;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Boolean getEtat() {
        return etat;
    }

    public void setEtat(Boolean etat) {
        this.etat = etat;
    }

    public Documents getDocuments() {
        return documents;
    }

    public void setDocuments(Documents documents) {
        this.documents = documents;
    }

    /*public Category getFolder() {
        return category;
    }

    public void setFolder(Category category) {
        this.category = category;
    }*/

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (documentsHasFolderPK != null ? documentsHasFolderPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DocumentsHasFolder)) {
            return false;
        }
        DocumentsHasFolder other = (DocumentsHasFolder) object;
        if ((this.documentsHasFolderPK == null && other.documentsHasFolderPK != null) || (this.documentsHasFolderPK != null && !this.documentsHasFolderPK.equals(other.documentsHasFolderPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.qualshore.etreasury.entity.DocumentsHasFolder[ documentsHasFolderPK=" + documentsHasFolderPK + " ]";
    }
    
}
