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
public class DocumentsHasRequestPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "documents_id_documents")
    private int documentsIdDocuments;
    @Basic(optional = false)
    @NotNull
    @Column(name = "request_id_demande")
    private int requestIdDemande;

    public DocumentsHasRequestPK() {
    }

    public DocumentsHasRequestPK(int documentsIdDocuments, int requestIdDemande) {
        this.documentsIdDocuments = documentsIdDocuments;
        this.requestIdDemande = requestIdDemande;
    }

    public int getDocumentsIdDocuments() {
        return documentsIdDocuments;
    }

    public void setDocumentsIdDocuments(int documentsIdDocuments) {
        this.documentsIdDocuments = documentsIdDocuments;
    }

    public int getRequestIdDemande() {
        return requestIdDemande;
    }

    public void setRequestIdDemande(int requestIdDemande) {
        this.requestIdDemande = requestIdDemande;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) documentsIdDocuments;
        hash += (int) requestIdDemande;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DocumentsHasRequestPK)) {
            return false;
        }
        DocumentsHasRequestPK other = (DocumentsHasRequestPK) object;
        if (this.documentsIdDocuments != other.documentsIdDocuments) {
            return false;
        }
        if (this.requestIdDemande != other.requestIdDemande) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.qualshore.etreasury.entity.DocumentsHasRequestPK[ documentsIdDocuments=" + documentsIdDocuments + ", requestIdDemande=" + requestIdDemande + " ]";
    }
    
}
