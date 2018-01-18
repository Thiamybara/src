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
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Proxy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author macbookpro
 */
@Entity
@Table(name = "log")
@JsonIgnoreProperties(ignoreUnknown = false)
@Proxy(lazy = false)
@XmlRootElement

public class Log implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_log")
    private Integer idLog;
 
    @Column(name = "description")
    private String description;
    
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    
    @Column(name = "id_user")
    private Integer idUser;
    
    @Column(name = "role")
    private Integer role;

    @Column(name = "ancienne_valeur")
    private String ancienneValeur;

    @Column(name = "nouvelle_valeur")
    private String nouvelleValeur;
    
    @Column(name = "id_request")
    private Integer idRequest;
    
    @Column(name = "id_offer")
    private Integer idOffer;
    
    @Column(name = "id_message")
    private Integer idMessage;
    
    @Column(name = "id_rate_day")
    private Integer idRateDay;
    
    @Column(name = "id_document")
    private Integer idDocument;
    
    @Column(name = "id_product")
    private Integer idProduct;
    
    @Column(name = "id_validation_level")
    private Integer idValidationLevel;
    
    @Column(name = "id_institution")
    private Integer idInstitution;
    
    @Column(name = "id_locality")
    private Integer idLocality;
    
    @Column(name = "id_group")
    private Integer idGroup;
    
    @Column(name = "id_profile")
    private Integer idProfile;
    
    @Column(name = "id_category")
    private Integer idCategory;
    
    @JoinColumn(name = "action_id_action", referencedColumnName = "id_action")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Action actionIdAction;

    public Log() {
    }

    public Log(Integer idLog) {
        this.idLog = idLog;
    }

    public Integer getIdLog() {
        return idLog;
    }

    public void setIdLog(Integer idLog) {
        this.idLog = idLog;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String parameter) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public String getAncienneValeur() {
        return ancienneValeur;
    }

    public void setAncienneValeur(String ancienneValeur) {
        this.ancienneValeur = ancienneValeur;
    }

    public String getNouvelleValeur() {
        return nouvelleValeur;
    }

    public void setNouvelleValeur(String nouvelleValeur) {
        this.nouvelleValeur = nouvelleValeur;
    }

    public Integer getIdRequest() {
        return idRequest;
    }

    public void setIdRequest(Integer idRequest) {
        this.idRequest = idRequest;
    }

    public Integer getIdOffer() {
        return idOffer;
    }

    public void setIdOffer(Integer idOffer) {
        this.idOffer = idOffer;
    }

    public Integer getIdMessage() {
        return idMessage;
    }

    public void setIdMessage(Integer idMessage) {
        this.idMessage = idMessage;
    }

    public Integer getIdRateDay() {
        return idRateDay;
    }

    public void setIdRateDay(Integer idRateDay) {
        this.idRateDay = idRateDay;
    }

    public Integer getIdDocument() {
        return idDocument;
    }

    public void setIdDocument(Integer idDocument) {
        this.idDocument = idDocument;
    }

    public Integer getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(Integer idProduct) {
        this.idProduct = idProduct;
    }

    public Integer getIdValidationLevel() {
        return idValidationLevel;
    }

    public void setIdValidationLevel(Integer idValidationLevel) {
        this.idValidationLevel = idValidationLevel;
    }

    /*public Integer getIdEntreprise() {
        return idEntreprise;
    }

    public void setIdEntreprise(Integer idEntreprise) {
        this.idEntreprise = idEntreprise;
    }

    public Integer getIdBank() {
        return idBank;
    }

    public void setIdBank(Integer idBank) {
        this.idBank = idBank;
    }*/

    public Integer getIdInstitution() {
        return idInstitution;
    }

    public void setIdInstitution(Integer idInstitution) {
        this.idInstitution = idInstitution;
    }

    public Integer getIdLocality() {
        return idLocality;
    }

    public void setIdLocality(Integer idLocality) {
        this.idLocality = idLocality;
    }

    public Integer getIdGroup() {
        return idGroup;
    }

    public void setIdGroup(Integer idGroup) {
        this.idGroup = idGroup;
    }

    public Integer getIdProfile() {
        return idProfile;
    }

    public void setIdProfile(Integer idProfile) {
        this.idProfile = idProfile;
    }

    public Integer getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(Integer idCategory) {
        this.idCategory = idCategory;
    }

    public Action getActionIdAction() {
        return actionIdAction;
    }

    public void setActionIdAction(Action actionIdAction) {
        this.actionIdAction = actionIdAction;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idLog != null ? idLog.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Log)) {
            return false;
        }
        Log other = (Log) object;
        if ((this.idLog == null && other.idLog != null) || (this.idLog != null && !this.idLog.equals(other.idLog))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.qualshore.etreasury.entity.Log[ idLog=" + idLog + " ]";
    }
    
}
