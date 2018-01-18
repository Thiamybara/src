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
@Table(name = "documents_has_request")
@JsonIgnoreProperties(ignoreUnknown = false)
@Proxy(lazy = false)
@XmlRootElement

public class DocumentsHasRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected DocumentsHasRequestPK documentsHasRequestPK;
    @Column(name = "date_document")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateDocument;
    @JoinColumn(name = "documents_id_documents", referencedColumnName = "id_documents", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Documents documents;
    @JoinColumn(name = "request_id_demande", referencedColumnName = "id_demande", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Request request;

    public DocumentsHasRequest() {
    }

    public DocumentsHasRequest(DocumentsHasRequestPK documentsHasRequestPK) {
        this.documentsHasRequestPK = documentsHasRequestPK;
    }

    public DocumentsHasRequest(int documentsIdDocuments, int requestIdDemande) {
        this.documentsHasRequestPK = new DocumentsHasRequestPK(documentsIdDocuments, requestIdDemande);
    }

    public DocumentsHasRequestPK getDocumentsHasRequestPK() {
        return documentsHasRequestPK;
    }

    public void setDocumentsHasRequestPK(DocumentsHasRequestPK documentsHasRequestPK) {
        this.documentsHasRequestPK = documentsHasRequestPK;
    }

    public Date getDateDocument() {
        return dateDocument;
    }

    public void setDateDocument(Date dateDocument) {
        this.dateDocument = dateDocument;
    }

    public Documents getDocuments() {
        return documents;
    }

    public void setDocuments(Documents documents) {
        this.documents = documents;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (documentsHasRequestPK != null ? documentsHasRequestPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DocumentsHasRequest)) {
            return false;
        }
        DocumentsHasRequest other = (DocumentsHasRequest) object;
        if ((this.documentsHasRequestPK == null && other.documentsHasRequestPK != null) || (this.documentsHasRequestPK != null && !this.documentsHasRequestPK.equals(other.documentsHasRequestPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.qualshore.etreasury.entity.DocumentsHasRequest[ documentsHasRequestPK=" + documentsHasRequestPK + " ]";
    }
    
}
