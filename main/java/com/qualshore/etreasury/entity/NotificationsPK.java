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
public class NotificationsPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "product")
    private int product;
    @Basic(optional = false)
    @NotNull
    @Column(name = "user")
    private int user;
    @Basic(optional = false)
    @NotNull
    @Column(name = "institution")
    private int institution;
    public NotificationsPK() {
    }

   
    public NotificationsPK(int product, int user, int institution) {
        this.product = product;
        this.user = user;
        this.institution = institution;
    }
    public int getProduct() {
        return product;
    }

    public void setProduct(int product) {
        this.product = product;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public int getInstitution() {
        return institution;
    }

    public void setInstitution(int institution) {
        this.institution = institution;
    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) product;
        hash += (int) user;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NotificationsPK)) {
            return false;
        }
        NotificationsPK other = (NotificationsPK) object;
        if (this.product != other.product) {
            return false;
        }
        if (this.user != other.user) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.qualshore.etreasury.entity.NotificationsPK[ product=" + product + ", user=" + user + " ]";
    }
    
}
