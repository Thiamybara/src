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
@Table(name = "documents_has_offer")
@JsonIgnoreProperties(ignoreUnknown = false)
@Proxy(lazy = false)
@XmlRootElement

public class DocumentsHasOffer implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @EmbeddedId
    protected DocumentsHasOfferPK documentsHasOfferPK;
    
    @Column(name = "date_document")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateDocument;
    
    @JoinColumn(name = "documents_id_documents", referencedColumnName = "id_documents", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Documents documents;
    
    @JoinColumn(name = "offer_id_offre", referencedColumnName = "id_offre", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Offer offer;

    public DocumentsHasOffer() {
    }

    public DocumentsHasOffer(DocumentsHasOfferPK documentsHasOfferPK) {
        this.documentsHasOfferPK = documentsHasOfferPK;
    }

    public DocumentsHasOffer(int documentsIdDocuments, int offerIdOffre) {
        this.documentsHasOfferPK = new DocumentsHasOfferPK(documentsIdDocuments, offerIdOffre);
    }

    public DocumentsHasOfferPK getDocumentsHasOfferPK() {
        return documentsHasOfferPK;
    }

    public void setDocumentsHasOfferPK(DocumentsHasOfferPK documentsHasOfferPK) {
        this.documentsHasOfferPK = documentsHasOfferPK;
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

    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (documentsHasOfferPK != null ? documentsHasOfferPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DocumentsHasOffer)) {
            return false;
        }
        DocumentsHasOffer other = (DocumentsHasOffer) object;
        if ((this.documentsHasOfferPK == null && other.documentsHasOfferPK != null) || (this.documentsHasOfferPK != null && !this.documentsHasOfferPK.equals(other.documentsHasOfferPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.qualshore.etreasury.entity.DocumentsHasOffer[ documentsHasOfferPK=" + documentsHasOfferPK + " ]";
    }
    
}
