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
public class DocumentsHasUserPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "documents_id_documents")
    private int documentsIdDocuments;
    @Basic(optional = false)
    @NotNull
    @Column(name = "user_id_utilisateur")
    private int userIdUtilisateur;

    public DocumentsHasUserPK() {
    }

    public DocumentsHasUserPK(int documentsIdDocuments, int userIdUtilisateur) {
        this.documentsIdDocuments = documentsIdDocuments;
        this.userIdUtilisateur = userIdUtilisateur;
    }

    public int getDocumentsIdDocuments() {
        return documentsIdDocuments;
    }

    public void setDocumentsIdDocuments(int documentsIdDocuments) {
        this.documentsIdDocuments = documentsIdDocuments;
    }

    public int getUserIdUtilisateur() {
        return userIdUtilisateur;
    }

    public void setUserIdUtilisateur(int userIdUtilisateur) {
        this.userIdUtilisateur = userIdUtilisateur;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) documentsIdDocuments;
        hash += (int) userIdUtilisateur;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DocumentsHasUserPK)) {
            return false;
        }
        DocumentsHasUserPK other = (DocumentsHasUserPK) object;
        if (this.documentsIdDocuments != other.documentsIdDocuments) {
            return false;
        }
        if (this.userIdUtilisateur != other.userIdUtilisateur) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.qualshore.etreasury.entity.DocumentsHasUserPK[ documentsIdDocuments=" + documentsIdDocuments + ", userIdUtilisateur=" + userIdUtilisateur + " ]";
    }
    
}
