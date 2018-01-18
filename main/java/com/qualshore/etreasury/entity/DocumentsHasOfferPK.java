/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qualshore.etreasury.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author macbookpro
 */
@Embeddable
public class DocumentsHasOfferPK implements Serializable {

	private static final long serialVersionUID = 1L;

	@Basic(optional = false)
    @NotNull
    @Column(name = "documents_id_documents")
    private int documentsIdDocuments;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "offer_id_offre")
    private int offerIdOffre;

    public DocumentsHasOfferPK() {
    }

    public DocumentsHasOfferPK(int documentsIdDocuments, int offerIdOffre) {
        this.documentsIdDocuments = documentsIdDocuments;
        this.offerIdOffre = offerIdOffre;
    }

    public int getDocumentsIdDocuments() {
        return documentsIdDocuments;
    }

    public void setDocumentsIdDocuments(int documentsIdDocuments) {
        this.documentsIdDocuments = documentsIdDocuments;
    }

    public int getOfferIdOffre() {
        return offerIdOffre;
    }

    public void setOfferIdOffre(int offerIdOffre) {
        this.offerIdOffre = offerIdOffre;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) documentsIdDocuments;
        hash += (int) offerIdOffre;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DocumentsHasOfferPK)) {
            return false;
        }
        DocumentsHasOfferPK other = (DocumentsHasOfferPK) object;
        if (this.documentsIdDocuments != other.documentsIdDocuments) {
            return false;
        }
        if (this.offerIdOffre != other.offerIdOffre) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.qualshore.etreasury.entity.DocumentsHasOfferPK[ documentsIdDocuments=" + documentsIdDocuments + ", offerIdOffre=" + offerIdOffre + " ]";
    }
    
}
