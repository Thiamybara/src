/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qualshore.etreasury.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Proxy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author macbookpro
 */
@Entity
@Table(name = "commission")
@JsonIgnoreProperties(ignoreUnknown = false)
@Proxy(lazy = false)
@XmlRootElement

public class Commission implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_commission")
    private Integer idCommission;
  
    @Column(name = "type")
    private String type;
   
    @Column(name = "nature")
    private String nature;

    @Column(name = "description")
    private String description;

    @Column(name = "commission_transfert")
    private String commissionTransfert;
    @JoinColumn(name = "taux_jour_id_taux_jour", referencedColumnName = "id_taux_jour")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private RateDay tauxJourIdTauxJour;

    public Commission() {
    }

    public Commission(Integer idCommission) {
        this.idCommission = idCommission;
    }

    public Integer getIdCommission() {
        return idCommission;
    }

    public void setIdCommission(Integer idCommission) {
        this.idCommission = idCommission;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCommissionTransfert() {
        return commissionTransfert;
    }

    public void setCommissionTransfert(String commissionTransfert) {
        this.commissionTransfert = commissionTransfert;
    }

    public RateDay getTauxJourIdTauxJour() {
        return tauxJourIdTauxJour;
    }

    public void setTauxJourIdTauxJour(RateDay tauxJourIdTauxJour) {
        this.tauxJourIdTauxJour = tauxJourIdTauxJour;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCommission != null ? idCommission.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Commission)) {
            return false;
        }
        Commission other = (Commission) object;
        if ((this.idCommission == null && other.idCommission != null) || (this.idCommission != null && !this.idCommission.equals(other.idCommission))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.qualshore.etreasury.entity.Commission[ idCommission=" + idCommission + " ]";
    }
    
}
