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
public class DocumentsHasFolderPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "documents_id_documents")
    private int documentsIdDocuments;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dossier_id_dossier")
    private int dossierIdDossier;

    public DocumentsHasFolderPK() {
    }

    public DocumentsHasFolderPK(int documentsIdDocuments, int dossierIdDossier) {
        this.documentsIdDocuments = documentsIdDocuments;
        this.dossierIdDossier = dossierIdDossier;
    }

    public int getDocumentsIdDocuments() {
        return documentsIdDocuments;
    }

    public void setDocumentsIdDocuments(int documentsIdDocuments) {
        this.documentsIdDocuments = documentsIdDocuments;
    }

    public int getDossierIdDossier() {
        return dossierIdDossier;
    }

    public void setDossierIdDossier(int dossierIdDossier) {
        this.dossierIdDossier = dossierIdDossier;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) documentsIdDocuments;
        hash += (int) dossierIdDossier;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DocumentsHasFolderPK)) {
            return false;
        }
        DocumentsHasFolderPK other = (DocumentsHasFolderPK) object;
        if (this.documentsIdDocuments != other.documentsIdDocuments) {
            return false;
        }
        if (this.dossierIdDossier != other.dossierIdDossier) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.qualshore.etreasury.entity.DocumentsHasFolderPK[ documentsIdDocuments=" + documentsIdDocuments + ", dossierIdDossier=" + dossierIdDossier + " ]";
    }
    
}
