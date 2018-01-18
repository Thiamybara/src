/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qualshore.etreasury.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Proxy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author macbookpro
 */

@Entity
@Table(name = "notifications")
@JsonIgnoreProperties(ignoreUnknown = false)
@Proxy(lazy = false)
@XmlRootElement

public class Notifications implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @EmbeddedId
    protected NotificationsPK notificationsPK;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    
    @JoinColumn(name = "institution", referencedColumnName = "id_institution", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Institution institution;
    
    @JoinColumn(name = "product", referencedColumnName = "id_produits", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Products products;
    
    @JoinColumn(name = "user", referencedColumnName = "id_utilisateur", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User user;

    public Notifications() {
    }

    public Notifications(NotificationsPK notificationsPK) {
        this.notificationsPK = notificationsPK;
    }

    public Notifications(NotificationsPK notificationsPK, Date date) {
        this.notificationsPK = notificationsPK;
        this.date = date;
    }

  
    public Notifications(int product, int user, int institution) {
        this.notificationsPK = new NotificationsPK(product, user, institution);
    }

    public Notifications(Products product, User user, Institution institution) {
        this.notificationsPK = new NotificationsPK(product.getIdProduits(), user.getIdUtilisateur(), institution.getIdInstitution());
        this.products = product;
        this.user = user;
        this.institution = institution;
        this.date = new Date();
    }

    public NotificationsPK getNotificationsPK() {
        return notificationsPK;
    }

    public void setNotificationsPK(NotificationsPK notificationsPK) {
        this.notificationsPK = notificationsPK;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    public Institution getInstitution() {
        return institution;
    }

    public void setInstitution(Institution institution) {
        this.institution = institution;
    }
    public Products getProducts() {
        return products;
    }

    public void setProducts(Products products) {
        this.products = products;
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
        hash += (notificationsPK != null ? notificationsPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Notifications)) {
            return false;
        }
        Notifications other = (Notifications) object;
        if ((this.notificationsPK == null && other.notificationsPK != null) || (this.notificationsPK != null && !this.notificationsPK.equals(other.notificationsPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.qualshore.etreasury.entity.Notifications[ notificationsPK=" + notificationsPK + " ]";
    }
    
}
